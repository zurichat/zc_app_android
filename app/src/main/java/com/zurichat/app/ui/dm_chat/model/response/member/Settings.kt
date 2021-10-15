package com.zurichat.app.ui.dm_chat.model.response.member

data class Settings(
    val chat_settings: ChatSettings,
    val messages_and_media: MessagesAndMedia,
    val notifications: Notifications,
    val sidebar: Sidebar,
    val themes: Themes
)