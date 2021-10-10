package com.zurichat.app.ui.dm_chat.model.request

data class SendMessageBody(
    val message: String,
    val room_id: String,
    val sender_id: String
)