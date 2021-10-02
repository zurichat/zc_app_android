package com.tolstoy.zurichat.ui.dm_chat.model.response.message

data class Data(
    val created_at: String,
    val message: String,
    val sender_id: String
)