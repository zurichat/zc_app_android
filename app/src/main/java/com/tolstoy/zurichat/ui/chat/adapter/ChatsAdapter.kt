package com.tolstoy.zurichat.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChatItemBinding
import com.tolstoy.zurichat.ui.chat.model.Chat


class ChatsAdapter(
    private val countries: List<Chat>,
    private val context: Context
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countries[position],context)
    }

    override fun getItemCount(): Int = countries.size

    inner class ViewHolder(private var item: ChatItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(chat: Chat,context: Context) {
            item.chatItemPersonNameTxt.text = chat.name
            item.chatItemPersonIcon.setBackgroundResource(chat.imageResource)
            item.chatItemMessageTxt.text = chat.message
            if (chat.name != context.getString(R.string.chat_item_mary_value) || chat.name != context.getString(R.string.chat_item_luxanne_value)){
                item.chatItemTimeTxt.setTextColor(ContextCompat.getColor(context,R.color.lightGray))
                item.chatItemMessageCountTxt.text = ""
                item.chatItemMessageCountTxt.setBackgroundResource(R.color.white)
            }
        }
    }
}
