package com.zurichat.app.ui.fragments.channel_chat

import android.app.Activity
import android.view.View
import androidx.collection.ArrayMap
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.ChannelIncomingMessageModelBinding
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.fragments.model.Data
import java.text.SimpleDateFormat
import java.util.*

data class ChannelListItem (val data: Data,val user:User, val context: Activity,val userMap: ArrayMap<String, OrganizationMember>) : BaseItem<ChannelIncomingMessageModelBinding> {
    override val layoutId = R.layout.channel_incoming_message_model
    override val uniqueId: String = data._id.toString()

    override fun initializeViewBinding(view: View) = ChannelIncomingMessageModelBinding.bind(view)

    override fun bind(binding: ChannelIncomingMessageModelBinding, itemClickCallback: ((BaseItem<ChannelIncomingMessageModelBinding>) -> Unit)?) {
        val s = SimpleDateFormat("hh:mm aa")
        val time = s.format(convertStringDateToLong(data.timestamp.toString()))
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }
        if (data.user_id.toString() == user.id){
            binding.bubble1.visibility = View.VISIBLE
            binding.bubble.visibility = View.GONE
            binding.imageParent.visibility = View.GONE

            binding.messageText1.text = data.content
            binding.messageTime1.text = time
        }else{
            binding.bubble1.visibility = View.GONE
            binding.bubble.visibility = View.VISIBLE
            binding.imageParent.visibility = View.VISIBLE

            binding.messageText.text = data.content
            binding.messageTime.text = time
            if (userMap.containsKey(data.user_id)){
                val orgMember = userMap[data.user_id]
                binding.messageAuthor.text = orgMember?.email
                if (!(orgMember?.imageUrl.isNullOrBlank())){
                    Glide
                        .with(context)
                        .load(orgMember?.imageUrl)
                        .into(binding.messageUserAvatar1)
                }
            }else{
                binding.messageAuthor.text = "No Name"
            }
            //binding.messageAuthor.text = data.user_id
        }

    }

    private fun convertStringDateToLong(date: String): Date {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        return s.parse(date)
    }

}