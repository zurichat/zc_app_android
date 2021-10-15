package com.zurichat.app.ui.fragments.home_screen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.ItemChatBinding
import com.zurichat.app.models.Room

class ChatsAdapter: RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    var onItemClickListener: ((Chat) -> Unit)? = null

    private val differCallback = object: DiffUtil.ItemCallback<Chat>(){
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.room.id == newItem.room.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(ItemChatBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        return holder.bind(differ.currentList[position])
    }

    fun addItems(chats: List<Chat>) = differ.submitList(chats)

    inner class ChatViewHolder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(chat: Chat) = with(binding){
            Glide.with(root.context.applicationContext).load(chat.dp)
                .placeholder(R.drawable.image_channel).into(imageChatUser)
            textChatLastMessage.text = chat.message
            textChatTime.text = chat.createdAt
            textChatUsername.text = chat.sender
            textChatUnreadMessages.text = chat.unreadCount.toString()

            root.setOnClickListener{
                onItemClickListener?.invoke(chat)
            }
        }
    }

    data class Chat(val sender: String, val dp: String, val room: Room,
                    val message: String, val createdAt: String, val unreadCount: Int)
}