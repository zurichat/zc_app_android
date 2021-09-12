package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import dev.ronnie.github.imagepicker.ImagePicker
import java.util.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import timber.log.Timber


class ChannelChatFragment : Fragment() {
    private lateinit var binding: FragmentChannelChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(view?.findViewById(R.id.channel_chat_toolbar))
//            setupActionBarWithNavController(findNavController())
        }
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(ChannelChatFragment()).navigateUp()
                    Timber.e("handleOnBackPressed")
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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