package com.zurichat.app.ui.dm_chat.model.request.createroom

data class CreateRoomBody(
    val org_id: String,
    val room_member_ids: List<String>,
    val room_name: String
)