package com.zurichat.app.ui.dm_chat.model.response.member

data class ChatSettings(
    val enter_is_send: Boolean,
    val font_size: String,
    val media_visibility: Boolean,
    val theme: String,
    val wallpaper: String
)