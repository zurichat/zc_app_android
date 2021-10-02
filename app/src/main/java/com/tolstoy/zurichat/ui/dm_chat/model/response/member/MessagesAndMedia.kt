package com.tolstoy.zurichat.ui.dm_chat.model.response.member

data class MessagesAndMedia(
    val additional_options: Any,
    val bring_emails_into_zuri: String,
    val convert_emoticons_to_emoji: Boolean,
    val custom: Boolean,
    val emoji: String,
    val emoji_as_text: Boolean,
    val frequently_used_emoji: Boolean,
    val inline_media_and_links: Any,
    val messages_one_click_reaction: Any,
    val names: String,
    val show_jumbomoji: Boolean,
    val theme: String
)