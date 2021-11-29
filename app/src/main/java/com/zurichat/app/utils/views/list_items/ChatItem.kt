package com.zurichat.app.utils.views.list_items

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.data.model.Chat
import com.zurichat.app.databinding.ItemChatBinding
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.show

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Nov-21 at 2:35 PM
 *
 * Represents a chat item on the home screen
 * @param chat the [Chat] model to bind to this class
 */
class ChatItem(
    private val chat: Chat
) : BaseItem<Chat, ItemChatBinding>(chat, R.layout.item_chat, chat.roomId) {

    override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)
    override fun bind(binding: ItemChatBinding): Unit = with(binding) {
        Glide.with(root.context).load(chat.image)
            .placeholder(R.drawable.ic_person).into(imageChatUser)
        imageChatOnline.isVisible = chat.online
        textChatUsername.text = chat.name
        textChatLastMessage.text = chat.message
        textChatTime.text = chat.time
        chatUnreadMessages.root.apply {
            if(chat.unread > 0) show()
            text = chat.unread.toString()
        }
    }
}