package com.tolstoy.zurichat.ui.fragments.channel_chat.data.model

import android.R.attr
import kotlin.jvm.JvmOverloads
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.*
import android.R.attr.author

class ChannelChatMessage @JvmOverloads constructor(
    private val id: String,
    private val user: ChannelUser,
    private var text: String,
    private var createdAt: Date = Date()
) : IMessage, MessageContentType.Image, MessageContentType {
    private var image: Image? = null
    var voice: Voice? = null

    override fun getId(): String {
        return id
    }

    override fun getText(): String {
        return text
    }

    override fun getCreatedAt(): Date {
        return createdAt
    }

    override fun getUser(): ChannelUser {
        return user
    }

    override fun getImageUrl(): String? {
        return if (image == null) null else image!!.url
    }

    val status: String
        get() = "Sent"

    fun setText(text: String) {
        this.text = text
    }

    fun setCreatedAt(createdAt: Date) {
        this.createdAt = createdAt
    }

    fun setImage(image: Image?) {
        this.image = image
    }

    class Image(var url: String)
    class Voice(var url: String, var duration: Int)
}