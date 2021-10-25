package com.zurichat.app.ui.dm.response

data class RoomListResponseItem(
    val _id: String,
    val bookmarks: List<String> = listOf(),
    val created_at: String = "",
    val org_id: String,
    val pinned: List<String> = listOf(),
    val room_user_ids: List<String>
)