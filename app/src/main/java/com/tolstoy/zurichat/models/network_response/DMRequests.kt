package com.tolstoy.zurichat.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 22-Sep-21
 */

data class CreateRoom(
    @SerializedName("org_id")
    @Expose
    val orgId: String, // 6145eee9285e4a18402074cd
    @SerializedName("room_user_ids")
    @Expose
    val roomUserIds: List<String>,
    @SerializedName("bookmarks")
    @Expose
    val bookmarks: List<Any> = listOf(),
    @SerializedName("pinned")
    @Expose
    val pinned: List<Any> = listOf()
)