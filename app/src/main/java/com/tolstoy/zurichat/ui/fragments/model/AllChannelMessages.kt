package com.tolstoy.zurichat.ui.fragments.model

import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel

data class AllChannelMessages(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)

data class Data(
    val _id: String,
    val can_reply: Any,
    val channel_id: String,
    val content: String,
    val edited: Any,
    val emojis: List<Emoji>,
    val event: Event,
    val files: List<String>,
    val has_files: Any,
    val pinned: Any,
    val replies: Int,
    val timestamp: String,
    val type: String,
    val user_id: String
)

data class Emoji(
    val count: Int,
    val title: String,
    val users: List<String>
)

data class Event(
    val action: String,
    val recipients: List<Recipient>
)

data class Recipient(
    val _id: String,
    val is_admin: Boolean,
    val notifications: Notifications,
    val role_id: String
)

class Notifications