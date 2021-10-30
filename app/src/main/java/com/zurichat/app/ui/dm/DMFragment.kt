package com.zurichat.app.ui.dm

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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentDmBinding
import com.zurichat.app.databinding.PartialAttachmentPopupBinding
import com.zurichat.app.models.Message
import com.zurichat.app.models.User
import com.zurichat.app.ui.dm.adapters.MessageAdapter
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm.dm_notification.DMNotificationWorker
import com.zurichat.app.util.setClickListener
import dagger.hilt.android.AndroidEntryPoint
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import hani.momanii.supernova_emoji_library.Helper.EmojiconsPopup
import kotlinx.coroutines.launch
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions


@AndroidEntryPoint
class DMFragment : Fragment(R.layout.fragment_dm) {

    private lateinit var imagePicker: ImagePicker
    private lateinit var adapter: MessageAdapter
    private val attachmentPopup by lazy { PopupWindow(requireContext()) }
    private val viewModel by viewModels<DMViewModel>()

    private lateinit var binding: FragmentDmBinding
    private lateinit var roomId: String
    private lateinit var userId: String
    private lateinit var senderId: String

    private lateinit var user : User
    private lateinit var roomList: RoomsListResponseItem
    private lateinit var emojiIconsActions: EmojIconActions


    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_send, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(requireContext().resources, android.R.drawable.ic_btn_speak_now, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePicker = ImagePicker(this)
        emojiIconsActions =
            EmojIconActions(context, view, binding.channelChatEditText, binding.iconBtn)
        emojiIconsActions.ShowEmojIcon()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDmBinding.bind(view)

        val bundle1 = arguments
        user = bundle1?.getParcelable("USER")!!
        roomList = bundle1.getParcelable("room")!!

        roomId = roomList._id
        userId = roomList.room_user_ids.first()
        senderId = roomList.room_user_ids.last()

        binding.iconBtn.setOnClickListener {
            Toast.makeText(context, getString(R.string.show_emoji), Toast.LENGTH_LONG).show()
        }

//        arguments?.let { bundle ->
//            val args = DMFragmentArgs.fromBundle(bundle)
//            roomId = args.roomId
//            userId = args.userId
//            senderId = args.senderId
//        }

//        setupObservers()
//        setupUI()
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
                attachmentPopup.showAtLocation(anchor, Gravity.BOTTOM or Gravity.START, 0, anchor.height)
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
//            viewModelScope.launch {
//                val newAdapter = MessageAdapter(requireContext(), userId, getMessages(roomId).messages.toMutableList(), true)
//                if(this@DMFragment::adapter.isInitialized){
//                    adapter.messages.forEach{ message ->
//                        newAdapter.addMessage(message)
//                    }
//                }
//                adapter = newAdapter
//                binding.listDm.adapter = adapter
//            }
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
//        findNavController().navigate(DMFragmentDirections.actionDmFragmentToAttachmentsFragment(media))
    }

    /**
     * Launches the camera activity to take pictures
     */
    private fun takePictureCamera() = imagePicker.takeFromCamera { imageResult ->
        when (imageResult) {
            is ImageResult.Success -> handleAttachmentUpload(listOf(imageResult.value))
            is ImageResult.Failure -> {
                Toast.makeText(requireContext(), getString(R.string.picture_not_taken), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun receiveAttachment(attachment: AttachmentsFragment.Attachment){
        handleAttachmentUpload(attachment.selected)
    }

    private fun handleAttachmentUpload(fileList: List<Uri>) = fileList.forEach{
        viewModel.uploadAttachment(it)
        displayMessage("", it)
    }


    private fun displayMessage(text: String, vararg media: Uri) =
        Message(message = text, senderId = userId, roomId = roomId ?: "").also{
            it.attachments.addAll(media)
            adapter.addMessage(it)
        }

    private fun sendMessage(text: String, vararg media: String) = with(viewModel) {
        viewModelScope.launch {
//            if(roomId == null){
//                roomId = createRoom("61467ee61a5607b13c00bcf2", "614f088ee35bb73a77bc2b70").roomId
//            }
            sendMessage(
                roomId, Message(
                    message = text, senderId = userId,
                    roomId = roomId, media = media.toList()
                )
            )
        }
    }

    /**
     * This function creates a workRequest to notify the receiver of a message
     * when a message is sent. It will be implemented fully when DM workflow
     * becomes functional.
     */
    private fun getWorkRequest(): WorkRequest {

        return OneTimeWorkRequestBuilder<DMNotificationWorker>().build()
    }
}
