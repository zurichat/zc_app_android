package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.databinding.ItemChatBinding
import com.tolstoy.zurichat.models.Message

class ChatsAdapter: RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {
    private val differCallback = object: DiffUtil.ItemCallback<Chat>(){
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.sender == newItem.sender
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    val chats: List<Chat> get() = differ.currentList

    private var onItemClickListener: ((Chat) -> Unit)? = null

    fun setItemClickListener(listener: (Chat) -> Unit) {
        onItemClickListener = listener
    }

    fun addItems(vararg chat: Chat) = differ.also {
        it.submitList(ArrayList(it.currentList).apply{
            addAll(chat)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(ItemChatBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(chat: Chat) = with(binding){
            Glide.with(binding.root.context.applicationContext)
                .load(chat.dp).into(binding.imageChatUser)
            textChatLastMessage.text = chat.message.message
            textChatTime.text = chat.message.createdAt
            textChatUsername.text = chat.sender
            textChatUnreadMessages.text = chat.unreadCount.toString()

            root.setOnClickListener{
                onItemClickListener?.invoke(chat)
            }
        }
    }

    data class Chat(val sender: String, val dp: String, val message: Message, val unreadCount: Int)
}