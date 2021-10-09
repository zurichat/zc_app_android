package com.zurichat.app.ui.dm_chat.model.response.message

data class Result(
    val created_at: String,
    val id: String,
    val media: List<Any>,
    val message: String,
    val pinned: Boolean,
    val reactions: List<Any>,
    val read: Boolean,
    val room_id: String,
    val saved_by: List<Any>,
    val sender_id: String,
    val threads: List<Any>
)