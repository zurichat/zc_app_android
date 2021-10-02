package com.tolstoy.zurichat.ui.dm.response

data class RoomListResponseItem(
    val _id: String,
    val bookmarks: List<String>,
    val created_at: String,
    val org_id: String,
    val pinned: List<String>,
    val room_user_ids: List<String>
)