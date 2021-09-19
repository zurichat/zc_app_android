package com.tolstoy.zurichat.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 01-Sep-21 at 6:03 PM
 *
 */
data class Message(
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null, // 2021-09-18T16:39:54.008Z

    @SerializedName("media")
    @Expose
    val media: List<String>? = null,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("pinned")
    @Expose
    val pinned: Boolean? = null,

    @SerializedName("read")
    @Expose
    val read: Boolean? = null,

    @SerializedName("room_id")
    @Expose
    val roomId: String,

    @SerializedName("saved_by")
    @Expose
    val savedBy: List<String>? = null,

    @SerializedName("sender_id")
    @Expose
    val senderId: String,

    @SerializedName("threads")
    @Expose
    val threads: List<Thread>? = null
)
