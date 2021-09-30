package com.tolstoy.zurichat.ui.fragments.model

data class Message(
    val user_id: String,
    val content: String,
    val files: List<String>?,
    val event:Event?,
)
