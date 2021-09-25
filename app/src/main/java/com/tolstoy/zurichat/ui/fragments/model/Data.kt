package com.tolstoy.zurichat.ui.fragments.model

data class Data(
    val _id: String?,
    val can_reply: Any?,
    val channel_id: String?,
    val content: String?,
    val edited: Any?,
    val emojis: List<Emoji>?,
    val event: Event?,
    val files: List<String>?,
    val has_files: Any?,
    val pinned: Any?,
    val replies: Int?,
    val timestamp: String?,
    val type: String?,
    val user_id: String?
)
