package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import dev.ronnie.github.imagepicker.ImagePicker

class ChannelChatFragment : Fragment() {
    private lateinit var binding: FragmentChannelChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val channelChatEdit = binding.channelChatEditText
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn


        channelChatEdit.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                sendMessage.isEnabled = false
                sendVoiceNote.isEnabled = true
            } else {
                sendMessage.isEnabled = true
                sendVoiceNote.isEnabled = false
            }
        }


        //initialize imagePicker library
        val imagePicker = ImagePicker(this)


        // TODO(Remove after test)

        binding.cameraChannelBtn.setOnClickListener {
            imagePicker.pickFromStorage {

            }
        }

        //Launch Attachment Popup
        val attachment = binding.channelLink

        //launch attachment dialog pop up
        attachment.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity())
            val view=layoutInflater.inflate(R.layout.dialog_attachment_layout,null)
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()

            val galleryIcon = view.findViewById<ImageView>(R.id.ic_gallery_attach)
            galleryIcon.setOnClickListener {
                imagePicker.pickFromStorage {

                }
            }
        }

    }
}