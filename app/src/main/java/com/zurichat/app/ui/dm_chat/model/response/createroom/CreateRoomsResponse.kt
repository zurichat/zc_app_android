package com.zurichat.app.ui.dm_chat.model.response.createroom

data class CreateRoomsResponse(
    val `data`: Data,
    val event: String,
    val plugin_id: String
)