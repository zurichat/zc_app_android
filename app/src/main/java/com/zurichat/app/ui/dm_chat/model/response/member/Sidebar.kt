package com.zurichat.app.ui.dm_chat.model.response.member

data class Sidebar(
    val always_show_in_the_sidebar: Any,
    val list_private_channels_seperately: Boolean,
    val organize_external_conversations: Boolean,
    val show_conversations: String,
    val show_profile_picture_next_to_dm: Boolean,
    val sidebar_sort: String
)