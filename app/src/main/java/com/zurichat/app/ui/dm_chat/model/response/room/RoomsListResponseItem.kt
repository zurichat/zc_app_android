package com.zurichat.app.ui.dm_chat.model.response.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RoomsListResponseItem(
    val _id: String,
    val bookmarks: List<String> = listOf(),
    val created_at: String = "",
    val org_id: String,
    val pinned: List<String> = listOf(),
    val room_user_ids: List<String>
): Parcelable