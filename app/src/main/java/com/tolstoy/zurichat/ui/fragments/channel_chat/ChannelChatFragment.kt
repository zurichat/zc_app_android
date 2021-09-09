package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
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
        val popupView: View = layoutInflater.inflate(R.layout.attachment_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
        }

    }
}