package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import dev.ronnie.github.imagepicker.ImagePicker

class ChannelChatFragment : Fragment() {
    private val viewModel : ChannelViewModel by viewModels()
    private lateinit var binding: FragmentChannelChatBinding
    private var user : User? = null
    private lateinit var channel: ChannelModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")
            channel = bundle.getParcelable("Channel")!!
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /**
        Temporary location to make network call to join channel. To be associated with the joinChannel button
        @Param: organizationId- organizationId will come from the clicked channel to join
        @Param: channelId - comes from channel to join
        @Param: user - creates a JoinChannelUser from the user Id, role_Id and adminRole
         */
        viewModel.joinChannel("1",channel._id, JoinChannelUser("cephas","manager"))

        viewModel.joinedUser.observe(viewLifecycleOwner,{joinedUser->
            if (joinedUser != null){
                Toast.makeText(requireContext(), "${joinedUser._id} Joined Channel Successfully", Toast.LENGTH_SHORT).show()
            }
        })

        val channelChatEdit = binding.channelChatEditText
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn
        val toolbar = view.findViewById<Toolbar>(R.id.channel_toolbar)

        toolbar.title = channel.name
        toolbar.subtitle = channel.members.toString().plus(" Members")
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

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
        val popupView: View = layoutInflater.inflate(R.layout.partial_attachment_popup, null)
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