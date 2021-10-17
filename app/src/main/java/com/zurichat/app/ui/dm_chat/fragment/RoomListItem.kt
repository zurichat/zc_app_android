package com.zurichat.app.ui.dm_chat.fragment

import android.app.Activity
import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.ChannelIncomingMessageModelBinding
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.dm_chat.model.response.message.BaseRoomData
import java.text.SimpleDateFormat
import java.util.*

data class RoomListItem(val data: BaseRoomData, val user: User, val context: Activity) :
    BaseItem<ChannelIncomingMessageModelBinding> {
    override val layoutId = R.layout.channel_incoming_message_model
    override var uniqueId: String = if (data.checkMessage) {
        data.sendMessageResponse!!.message_id
    } else {
        data.getMessageResponse!!.id
    }

    override fun initializeViewBinding(view: View) = ChannelIncomingMessageModelBinding.bind(view)

    override fun bind(binding: ChannelIncomingMessageModelBinding, itemClickCallback: ((BaseItem<ChannelIncomingMessageModelBinding>) -> Unit)?) {
        val s = SimpleDateFormat("hh:mm aa")
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }

        binding.messageAuthor.visibility = View.GONE
        binding.imageParent.visibility = View.GONE

        if ((data.checkMessage)) {
            val time = s.format(convertStringDateToLong(data.sendMessageResponse!!.data.created_at))

            binding.bubble1.visibility = View.VISIBLE
            binding.bubble.visibility = View.GONE

            binding.messageText1.text = data.sendMessageResponse.data.message
            binding.messageAuthor.text = data.sendMessageResponse.data.sender_id
            binding.messageTime1.text = time
        } else {
            val time = s.format(convertStringDateToLong(data.getMessageResponse!!.created_at))

            binding.bubble.visibility = View.VISIBLE
            binding.bubble1.visibility = View.GONE

            binding.messageText.text = data.getMessageResponse.message
            //binding.messageAuthor.text = data.getMessageResponse.sender_id
            binding.messageTime.text = time
        }
    }

    private fun convertStringDateToLong(date: String): Date {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        return s.parse(date)
    }

}