package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
import dev.ronnie.github.imagepicker.ImagePicker

class ChannelChatFragment : Fragment() {
    private val viewModel : ChannelViewModel by viewModels()
    private lateinit var binding: FragmentChannelChatBinding
    private var user : User? = null
    private lateinit var channel: ChannelModel
    private var channelJoined = false

    private var isEnterSend: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")
            channel = bundle.getParcelable("Channel")!!
            channelJoined = bundle.getBoolean("Channel Joined")
        }

        isEnterSend = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getBoolean("enter_to_send", false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // code to control the dimming of background
        val prefMngr = PreferenceManager.getDefaultSharedPreferences(context)
        val dimVal = prefMngr.getInt("bar",50).toFloat().div(100f)

        val dimmerBox = binding.dmChatDimmer
        val channelChatEdit = binding.channelChatEditText           //get message from this edit text
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn                    //use this button to send the message
        val typingBar = binding.channelTypingBar
        val toolbar = view.findViewById<Toolbar>(R.id.channel_toolbar)

        val imagePicker = ImagePicker(this)

        //val includeAttach = binding.attachment
        val attachment = binding.channelLink
        val popupView: View = layoutInflater.inflate(R.layout.partial_attachment_popup, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dimmerBox.alpha = dimVal

        if (channelJoined){
            binding.channelJoinBar.visibility = View.GONE
        }else{
            binding.channelName.text = channel.name

            if (channel.isPrivate){
                binding.channelName.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_new_lock),null,null,null)
            }else {
                binding.channelName.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_hash),null,null,null)
            }

            binding.channelJoinBar.visibility = View.VISIBLE

            binding.joinChannel.setOnClickListener {
                binding.joinChannel.visibility = View.GONE
                binding.text2.visibility = View.GONE
                binding.channelName.visibility = View.GONE
                binding.progressBar2.visibility = View.VISIBLE
                user?.let { JoinChannelUser(it.id,"manager") }?.let { viewModel.joinChannel("1",channel._id, it) }
            }

            viewModel.joinedUser.observe(viewLifecycleOwner,{joinedUser->
                if (joinedUser != null){
                    toolbar.subtitle = channel.members.plus(1).toString().plus(" Members")
                    Toast.makeText(requireContext(), "Joined Channel Successfully", Toast.LENGTH_SHORT).show()
                    binding.channelJoinBar.visibility = View.GONE
                }else{
                    binding.joinChannel.visibility = View.VISIBLE
                    binding.text2.visibility = View.VISIBLE
                    binding.channelName.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(requireContext(),getString(R.string.an_error_occured),Toast.LENGTH_SHORT).show()
                }
            })
        }

        toolbar.title = channel.name
        if (channel.members>1){
            toolbar.subtitle = channel.members.toString().plus(" Members")
        }else{
            toolbar.subtitle = channel.members.toString().plus(" Member")
        }
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

        sendMessage.setOnClickListener{
//  TODO(check if channelChatEdit is null or empty, and do nothing else, get the _id of the user that sent the message from user variable, get the string message from the edit text, send the to show up as one of the list items on the recyclerview in that)
        }

        binding.cameraChannelBtn.setOnClickListener {
            imagePicker.pickFromStorage {

            }
        }

        //Launch Attachment Popup
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            //popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
            popupWindow.showAsDropDown(typingBar,0,-(typingBar.height*4),Gravity.TOP)
        }

        setupKeyboard()
    }

    private fun setupKeyboard() {
        // set keyboard to send if "enter is send" is set to true in settings
        binding.channelChatEditText.apply {
            if(isEnterSend) {
                this.inputType = InputType.TYPE_CLASS_TEXT
                this.imeOptions = EditorInfo.IME_ACTION_SEND
            }
        }

        binding.channelChatEditText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEND) {
                // send message

                true
            } else {
                false
            }
        }
    }

}