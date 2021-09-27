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
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.databinding.FragmentDmBinding
import com.tolstoy.zurichat.databinding.PartialAttachmentPopupBinding
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.base.ViewModelFactory
import com.tolstoy.zurichat.ui.dm.adapters.MessageAdapter
import com.tolstoy.zurichat.util.setClickListener
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult

class DMFragment : Fragment(R.layout.fragment_dm) {

    private lateinit var imagePicker: ImagePicker
    private val adapter by lazy { MessageAdapter(requireContext(), 0) }
    private lateinit var binding: FragmentDmBinding
    private val attachmentPopup by lazy { PopupWindow(requireContext()) }
    private val user by lazy { Cache.map["user"] as? User }
    private val viewModel by viewModels<DMViewModel> { ViewModelFactory.INSTANCE }

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
        setupUI()
        setupObservers()

        // code to control the dimming of background
        val dimmerBox:View? = view?.findViewById<View>(R.id.dimmer_background)
        val prefMngr = PreferenceManager.getDefaultSharedPreferences(context)
        val dimVal = prefMngr.getInt("bar",50).toFloat().div(100f)
        dimmerBox?.alpha = dimVal

    }

    override fun onPause() {
        attachmentPopup.dismiss()
        super.onPause()
    }

    private fun setupUI() {
        // set up the message input view
        binding.messageinputDm.also {
            it.fabMIRecordAudio.setOnClickListener { _->
                it.textinputMIMessage.text.also { editable ->
                    if(editable.isNotBlank()) {
                        val message = Message(
                            message = editable.toString(),
                            senderId = user!!.id,
                            roomId = user!!.id
                        )
                        adapter.addMessage(message)
                        viewModel.sendMessage(message.roomId,message)
                        editable.clear()
                    }
                }
            }
            it.imageMIAttachImage.setOnClickListener { takePictureCamera() }
            it.imageMIAttachFile.setOnClickListener { _ ->
                val anchor = binding.messageinputDm.root
//                val location = IntArray(2).apply { anchor.getLocationOnScreen(this) }
//                val size = Size(attachmentPopup.contentView.measuredWidth, attachmentPopup.contentView.measuredHeight)
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
        binding.listDm.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        // set up the attachment popup
        PartialAttachmentPopupBinding.inflate(layoutInflater, binding.root, false).also {
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

    private fun setupObservers(){
        viewModel.imageUploadResonse.observe(viewLifecycleOwner){
            if (it.success){
                Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_LONG).show()
            } }
        viewModel.sendMessageResponse.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToAttachmentScreen(media: MEDIA = MEDIA.IMAGE){
        findNavController().navigate(DMFragmentDirections.actionDmFragmentToAttachmentsFragment(media))
    }

    /**function to take image using camera
    we are using a library to easen the work
     */
    private fun takePictureCamera() {
        //take image
        imagePicker.takeFromCamera { imageResult ->
            /**
             * when we take an image successfully,we take the uri and load using glide
             * we also set the view to visible
             */

            when (imageResult) {
                is ImageResult.Success -> handleImageUpload(listOf(imageResult.value))

                /**
                 * incase it's unsuccessful we toast the message and hide the image view
                 */
                is ImageResult.Failure -> {
//                    imageView.visibility = View.GONE
                    Toast.makeText(requireContext(), "Picture not taken", Toast.LENGTH_LONG).show()
                }
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
        adapter.addMessage(
            Message(
                message = "",
                senderId = user!!.id,
                roomId = user!!.id,
                media = listOf(it.toString())
            )
        )
        viewModel.uploadImage(requireContext().applicationContext, it)
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
}
