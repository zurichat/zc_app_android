package com.tolstoy.zurichat.ui.fragments.channel_chat.data.model

import com.stfalcon.chatkit.commons.models.IDialog
import java.util.*

class ChannelDialog(
    private val id: String,
    private val dialogName: String,
    private val dialogPhoto: String,
    private val users: ArrayList<ChannelUser>,
    private var lastMessage: ChannelChatMessage,
    private var unreadCount: Int
) : IDialog<ChannelChatMessage> {
    override fun getId(): String {
        return id
    }

    override fun getDialogPhoto(): String {
        return dialogPhoto
    }

    override fun getDialogName(): String {
        return dialogName
    }

    override fun getUsers(): ArrayList<ChannelUser> {
        return users
    }

    override fun getLastMessage(): ChannelChatMessage {
        return lastMessage
    }

    override fun setLastMessage(lastMessage: ChannelChatMessage) {
        this.lastMessage = lastMessage
    }

    override fun getUnreadCount(): Int {
        return unreadCount
    }

    fun setUnreadCount(unreadCount: Int) {
        this.unreadCount = unreadCount
    }
}