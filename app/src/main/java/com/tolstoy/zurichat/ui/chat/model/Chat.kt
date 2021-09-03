package com.tolstoy.zurichat.ui.chat.model

/*
chat item model
 */
class Chat(
    val name: String,
    val message: String,
    val time: String,
    val imageResource: Int,
    val messageCount: Int = 0
)