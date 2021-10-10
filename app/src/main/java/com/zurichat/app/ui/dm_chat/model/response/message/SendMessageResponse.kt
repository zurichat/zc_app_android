package com.zurichat.app.ui.dm_chat.model.response.message

data class SendMessageResponse(
    val `data`: Data,
    val event: String,
    val message_id: String,
    val room_id: String,
    val status: String,
    val thread: Boolean
)