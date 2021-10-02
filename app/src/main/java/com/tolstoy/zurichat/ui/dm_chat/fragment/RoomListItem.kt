package com.tolstoy.zurichat.ui.dm_chat.fragment

import android.app.Activity
import android.view.View
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChannelIncomingMessageModelBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.add_channel.BaseItem
import com.tolstoy.zurichat.ui.dm_chat.model.response.message.BaseRoomData
import com.tolstoy.zurichat.ui.fragments.model.Data
import java.text.SimpleDateFormat
import java.util.*

data class RoomListItem(val data: BaseRoomData, val user: User, val context: Activity) :
    BaseItem<ChannelIncomingMessageModelBinding> {
    override val layoutId = R.layout.channel_incoming_message_model
    override var uniqueId: String = if (data.checkMessage) {
        data.getMessageResponse!!.id
    } else {
        data.sendMessageResponse!!.message_id
    }


    override fun initializeViewBinding(view: View) = ChannelIncomingMessageModelBinding.bind(view)

    override fun bind(binding: ChannelIncomingMessageModelBinding, itemClickCallback: ((BaseItem<ChannelIncomingMessageModelBinding>) -> Unit)?) {
        val s = SimpleDateFormat("hh:mm aa")
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }
//        if (data.user_id.toString() == user.id){
//            binding.bubble1.visibility = View.VISIBLE
//            binding.bubble.visibility = View.GONE
//            binding.imageParent.visibility = View.GONE
//
//            binding.messageText1.text = data.content
//            binding.messageTime1.text = time
//        }else{
//            binding.bubble1.visibility = View.GONE
//            binding.bubble.visibility = View.VISIBLE
//            binding.imageParent.visibility = View.VISIBLE
//
//            binding.messageText.text = data.content
//            binding.messageTime.text = time
//            binding.messageAuthor.text = data.user_id
//        }


        if (data.checkMessage) {
            val time = s.format(convertStringDateToLong(data.getMessageResponse!!.created_at))

            binding.bubble1.visibility = View.GONE
            binding.bubble.visibility = View.VISIBLE

            binding.messageText1.text = data.getMessageResponse.message
            binding.messageTime1.text = time
        } else {
            val time = s.format(convertStringDateToLong(data.sendMessageResponse!!.data.created_at))

            binding.bubble1.visibility = View.VISIBLE
            binding.bubble.visibility = View.GONE

            binding.messageText1.text = data.sendMessageResponse.data.message
            binding.messageTime1.text = time
        }

        binding.imageParent.visibility = View.GONE


    }

    private fun convertStringDateToLong(date: String): Date {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        s.timeZone = TimeZone.getTimeZone("UTC")
        return s.parse(date)
    }
}