package com.zurichat.app.models

import android.net.Uri
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 01-Sep-21 at 6:03 PM
 *
 */
data class Message(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 2021-09-18T16:39:54.008Z

    @SerializedName("sender_id")
    @Expose
    val senderId: String,

    @SerializedName("room_id")
    @Expose
    val roomId: String,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("media")
    @Expose
    private val media: List<String> = emptyList(),

    @SerializedName("read")
    @Expose
    val read: Boolean = false,

    @SerializedName("pinned")
    @Expose
    val pinned: Boolean = false,

    @SerializedName("saved_by")
    @Expose
    val savedBy: List<String> = emptyList(),

    @SerializedName("threads")
    @Expose
    val threads: List<Thread> = emptyList(),

    @SerializedName("reactions")
    @Expose
    val reactions: List<Reaction> = emptyList()
){
    var fontSize: Float? = 0f
    val attachments = mutableListOf<Uri>().apply{
        addAll(media.map { Uri.parse(it) })
    }
}
