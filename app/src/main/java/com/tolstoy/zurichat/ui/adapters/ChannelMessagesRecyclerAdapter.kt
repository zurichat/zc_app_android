package com.tolstoy.zurichat.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.DmMessages

class ChannelMessagesRecyclerAdapter(
    private val channelMessagesList: MutableList<DmMessages>,
    private val currentUser: String
    ) : RecyclerView.Adapter<ChannelMessagesRecyclerAdapter.ChannelMessagesRecyclerViewHolder>(){



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChannelMessagesRecyclerViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ChannelMessagesRecyclerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ChannelMessagesRecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val channelChatLeftView: MaterialCardView = itemView.findViewById(R.id.channel_chat_left_view)
        private val channelChatLeftText: TextView = itemView.findViewById(R.id.channel_chat_left_text)
        private val channelChatLeftTextTime: TextView =
            itemView.findViewById(R.id.channel_chat_left_text_time)
        val channelChatRightView: MaterialCardView =
            itemView.findViewById(R.id.channel_chat_right_view)
        private val channelChatRightText: TextView = itemView.findViewById(R.id.channel_chat_right_text)
        private val channelChatRightTextTime: TextView =
            itemView.findViewById(R.id.channel_chat_right_text_time)

        fun bind(channelMessages: DmMessages, currentUser: String) {
            if (channelMessages.sender == currentUser) {
                channelChatLeftView.visibility = View.GONE
                channelChatRightView.visibility = View.VISIBLE
                channelChatRightText.text = channelMessages.message
                channelChatRightTextTime.text = channelMessages.time
            } else {
                channelChatRightView.visibility = View.GONE
                channelChatLeftView.visibility = View.VISIBLE
                channelChatLeftText.text = channelMessages.message
                channelChatLeftTextTime.text = channelMessages.time
            }
        }
    }
}