package com.zurichat.app.ui.dm_chat.model.response.createroom

data class Data(
    val ID: String,
    val button_url: String,
    val group_name: String,
    val joined_rooms: List<JoinedRoom>,
    val name: String,
    val public_rooms: List<Any>,
    val show_group: Boolean
)