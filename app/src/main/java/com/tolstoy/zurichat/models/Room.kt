package com.tolstoy.zurichat.models

import android.provider.ContactsContract
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
data class Room(
    @SerializedName("org_id")
    @Expose
    val organizationId: String,

    @SerializedName("room_user_ids")
    @Expose
    val roomUserIds: List<String>,

    @SerializedName("bookmarks")
    @Expose
    val bookmarks: List<String>,

    @SerializedName("pinned")
    @Expose
    val pinned: List<String>,

    @SerializedName("created_at")
    @Expose
    val createdAt: String
)
