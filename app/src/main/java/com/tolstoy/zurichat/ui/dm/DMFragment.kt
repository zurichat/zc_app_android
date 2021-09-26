package com.tolstoy.zurichat.ui.dm

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentDmBinding
import com.tolstoy.zurichat.databinding.PartialAttachmentPopupBinding
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.ui.dm.adapters.MessageAdapter
import com.tolstoy.zurichat.util.setClickListener
import dagger.hilt.android.AndroidEntryPoint
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DMFragment : Fragment(R.layout.fragment_dm) {

    private lateinit var imagePicker: ImagePicker
    private lateinit var adapter: MessageAdapter
    private val attachmentPopup by lazy { PopupWindow(requireContext()) }
    private val viewModel by viewModels<DMViewModel>()

    private lateinit var binding: FragmentDmBinding
    private var roomId: String? = null
    private lateinit var userId: String
    private lateinit var senderId: String

    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_send, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(requireContext().resources, android.R.drawable.ic_btn_speak_now, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePicker = ImagePicker(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDmBinding.bind(view)

        arguments?.let { bundle ->
            val args = DMFragmentArgs.fromBundle(bundle)
            roomId = args.roomId
            userId = args.userId
            senderId = args.senderId
        }

        setupObservers()
        setupUI()
    }

    override fun onPause() {
        attachmentPopup.dismiss()
        super.onPause()
    }

    private fun setupUI() = with(binding) {
        dimmerBackground.alpha = PreferenceManager.getDefaultSharedPreferences(context)
            .getInt("bar",50).toFloat().div(100f)

        // set up the message input view
        messageinputDm.also {
            it.fabMIRecordAudio.setOnClickListener { _->
                it.textinputMIMessage.text.also { editable ->
                    if(editable.isNotBlank()) {
                        val text = editable.toString()
                        displayMessage(text)
                        sendMessage(text)
                        editable.clear()
                    }
                }
            }
            it.imageMIAttachImage.setOnClickListener { takePictureCamera() }
            it.imageMIAttachFile.setOnClickListener {
                val anchor = messageinputDm.root
//                val location = IntArray(2).apply { anchor.getLocationOnScreen(this) }
                attachmentPopup.showAtLocation(anchor,Gravity.BOTTOM or Gravity.START, 0, anchor.height)
            }
            it.textinputMIMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    // checks if the the user types in a something
                    // if they do change the fab drawable
                    if(p0?.isNotBlank() == true)
                        it.fabMIRecordAudio.setImageDrawable(sendDrawable)
                    else it.fabMIRecordAudio.setImageDrawable(speakDrawable)
                }
            })
        }
        // set up the message recycler view
        listDm.also {
            // only initialize the adapter if it is not already initialized
            if(!this@DMFragment::adapter.isInitialized)
                adapter = MessageAdapter(requireContext(), userId)

            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        // set up the attachment popup
        PartialAttachmentPopupBinding.inflate(layoutInflater, root, false).also {
            it.groupGallery.setClickListener { navigateToAttachmentScreen() }
            it.groupAudio.setClickListener { navigateToAttachmentScreen(MEDIA.AUDIO) }
            it.groupDocument.setClickListener { navigateToAttachmentScreen(MEDIA.DOCUMENT) }
            attachmentPopup.apply {
                contentView = it.root
                isOutsideTouchable = true
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                setBackgroundDrawable(ColorDrawable())
            }
        }

        // observe values from the attachment fragment
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<AttachmentsFragment.Attachment>(AttachmentsFragment.Attachment.TAG)?.observe(
                viewLifecycleOwner){ receiveAttachment(it) }
    }

    private fun setupObservers() = with(viewModel){
        // retrieve messages if the room id is not null
        roomId?.let {
            viewModelScope.launch {
                val newAdapter = MessageAdapter(requireContext(), userId, getMessages(it).messages.toMutableList())
                if(this@DMFragment::adapter.isInitialized){
                    adapter.messages.forEach{ message ->
                        newAdapter.addMessage(message)
                    }
                }
                adapter = newAdapter
                binding.listDm.adapter = adapter
            }
        }
        attachmentUploadResponse.observe(viewLifecycleOwner){
            // image was uploaded successfully
            if (it.status == 200){
                // send the image message to the server
                sendMessage("", it.data.fileUrl)
            }
        }
        sendMessageResponse.observe(viewLifecycleOwner){}
    }

    private fun navigateToAttachmentScreen(media: MEDIA = MEDIA.IMAGE){
        findNavController().navigate(DMFragmentDirections.actionDmFragmentToAttachmentsFragment(media))
    }

    /**
     * Launches the camera activity to take pictures
     */
    private fun takePictureCamera() = imagePicker.takeFromCamera { imageResult ->
        when (imageResult) {
            is ImageResult.Success -> handleImageUpload(listOf(imageResult.value))
            is ImageResult.Failure -> {
                Toast.makeText(requireContext(), "Picture not taken", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun receiveAttachment(attachment: AttachmentsFragment.Attachment){
        when (attachment.media) {
            MEDIA.IMAGE -> handleImageUpload(attachment.selected)
            MEDIA.AUDIO -> handleAudioUpload(attachment.selected)
            MEDIA.DOCUMENT -> handleDocumentUpload(attachment.selected)
            else -> handleImageUpload(attachment.selected)
        }
    }

    private fun handleImageUpload(imageList: List<Uri>) = imageList.forEach{
        viewModel.uploadAttachment(it)
        displayMessage("", it)
    }

    private fun handleAudioUpload(audioList: List<Uri>) = audioList.forEach {
        adapter.addMessage(
            Message(
                message = "",
                senderId = user!!.id,
                roomId = user!!.id,
                media = listOf(it.toString())
            )
        )
    }

    private fun handleDocumentUpload(audioList: List<Uri>) = audioList.forEach {
        adapter.addMessage(
            Message(
                message = "",
                senderId = user!!.id,
                roomId = user!!.id,
                media = listOf(it.toString())
            )
        )
    }

    private fun displayMessage(text: String, vararg media: Uri) =
        Message(message = text, senderId = userId, roomId = roomId ?: "").also{
            it.attachments.addAll(media)
            adapter.addMessage(it)
        }

    private fun sendMessage(text: String, vararg media: String) = with(viewModel) {
        viewModelScope.launch {
            if(roomId == null){
                roomId = createRoom(userId, senderId).roomId
            }
            sendMessage(roomId!!, Message(message = text, senderId = userId,
                roomId = roomId!!, media = media.toList()))
        }
    }
}
