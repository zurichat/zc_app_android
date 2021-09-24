package com.tolstoy.zurichat.ui.fragments.channel_chat.data.model

import com.stfalcon.chatkit.commons.models.IUser

class ChannelUser(
    private var id: String,
    private var name: String,
    private var avatar: String,
    var isOnline: Boolean
) : IUser {
    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getAvatar(): String {
        return avatar
    }
}