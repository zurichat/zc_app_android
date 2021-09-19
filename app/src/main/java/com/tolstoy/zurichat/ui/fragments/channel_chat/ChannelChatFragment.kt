package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.ronnie.github.imagepicker.ImagePicker


@AndroidEntryPoint
class ChannelChatFragment : Fragment(R.layout.fragment_channel_chat) {
    private val viewModel: ChannelViewModel by viewModels()
    private val binding: FragmentChannelChatBinding by viewBinding(FragmentChannelChatBinding::bind)

    private lateinit var channel: ChannelModel
    private var channelJoined = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            channelJoined = it.getBoolean("Channel Joined")
            channel = it.getParcelable("Channel")!!
        }
        setupObservers()

        val channelChatEdit = binding.channelChatEditText  // get message from this edit text
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn    //use this button to send the message
        val toolbar = view.findViewById<Toolbar>(R.id.channel_toolbar)

        toolbar.title = channel.name
        toolbar.subtitle = channel.members.toString().plus(" Members")
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        channelChatEdit.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                sendMessage.isEnabled = false
                sendVoiceNote.isEnabled = true
            } else {
                sendMessage.isEnabled = true
                sendVoiceNote.isEnabled = false
            }
        }

//        OnclickListener for the sendMessageBtn to send message to the channel
        sendMessage.setOnClickListener {
//  TODO(check if channelChatEdit is null or empty, and do nothing else, get the _id of the user that sent the message from user variable, get the string message from the edit text, send the to show up as one of the list items on the recyclerview in that)
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

    private fun setupObservers() = with(binding) {
        /**
        Temporary location to make network call to join channel. To be associated with the joinChannel button
        @Param: organizationId- organizationId will come from the clicked channel to join
        @Param: channelId - comes from channel to join
        @Param: user - creates a JoinChannelUser from the user Id, role_Id and adminRole
         */
        viewModel.userItem.observe(viewLifecycleOwner) { user ->
            if (channelJoined) {
                channelJoinBar.visibility = View.GONE
            } else {
                joinChannel.setOnClickListener {
                    joinChannel.visibility = View.GONE
                    text2.visibility = View.GONE
                    channelName.visibility = View.GONE
                    progressBar2.visibility = View.VISIBLE
                    user?.let { JoinChannelUser(it.id, "manager") }
                        ?.let { viewModel.joinChannel("1", channel._id, it) }
                }
            }
            viewModel.joinedUser.observe(viewLifecycleOwner) { joinedUser ->
                if (joinedUser != null) {
                    Toast.makeText(
                        requireContext(),
                        "Joined Channel Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    channelJoinBar.visibility = View.GONE
                } else {
                    joinChannel.visibility = View.VISIBLE
                    text2.visibility = View.VISIBLE
                    channelName.visibility = View.VISIBLE
                    progressBar2.visibility = View.GONE
                }
            }
        }
    }

}