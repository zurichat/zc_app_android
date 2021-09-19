package com.tolstoy.zurichat.ui.dm

import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentDmBinding
import com.tolstoy.zurichat.databinding.PartialAttachmentPopupBinding
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.ui.dm.adapters.MessageAdapter
import com.tolstoy.zurichat.util.setClickListener
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult

class DMFragment : Fragment(R.layout.fragment_dm) {

    private lateinit var imagePicker: ImagePicker
    private val adapter by lazy { MessageAdapter(requireContext(), 0) }
    private lateinit var binding: FragmentDmBinding
    private val attachmentPopup by lazy { PopupWindow(requireContext()) }

    private var isEnterSend: Boolean = false

    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_send, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(
            requireContext().resources,
            android.R.drawable.ic_btn_speak_now,
            null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagePicker = ImagePicker(this)

        isEnterSend = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getBoolean("enter_to_send", false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDmBinding.bind(view)
        setupUI()
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

        // set keyboard to send if "enter is send" is set to true in settings
        binding.messageinputDm.textinputMIMessage.apply {
            if(isEnterSend) {
                this.inputType = InputType.TYPE_CLASS_TEXT
                this.imeOptions = EditorInfo.IME_ACTION_SEND
            }
        }

        binding.messageinputDm.textinputMIMessage.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage(binding.messageinputDm.textinputMIMessage.editableText)
                true
            } else {
                false
            }
        }


        // set up the message input view
        binding.messageinputDm.also {
            it.fabMIRecordAudio.setOnClickListener { _ ->
                it.textinputMIMessage.text.also { editable ->
                    sendMessage(editable)
                }
            }
            it.imageMIAttachImage.setOnClickListener { takePictureCamera() }
            it.imageMIAttachFile.setOnClickListener { _ ->
                val anchor = binding.messageinputDm.root
//                val location = IntArray(2).apply { anchor.getLocationOnScreen(this) }
//                val size = Size(attachmentPopup.contentView.measuredWidth, attachmentPopup.contentView.measuredHeight)
                attachmentPopup.showAtLocation(
                    anchor, Gravity.BOTTOM or Gravity.START,
                    0, anchor.height
                )
            }
            it.textinputMIMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    // checks if the the user types in a something
                    // if they do change the fab drawable
                    if (p0?.isNotBlank() == true)
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
    }

    private fun sendMessage(editable: Editable) {
        if (editable.isNotBlank()) {
            adapter.addMessage(Message(0, editable.toString()))
            editable.clear()
        }
    }

    private fun navigateToAttachmentScreen(media: MEDIA = MEDIA.IMAGE) {
        findNavController().navigate(
            DMFragmentDirections.actionDmFragmentToAttachmentsFragment(
                media
            )
        )
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
                is ImageResult.Success -> {
//                    val uri = imageResult.value
//
//                    imageView.visibility = View.VISIBLE
//                    Glide.with(this)
//                        .load(uri)
//                        .into(imageView)

                }

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
}
