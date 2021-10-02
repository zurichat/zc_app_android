package com.tolstoy.zurichat.models

import android.net.Uri
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 01-Sep-21 at 6:03 PM
 *
 */
@Entity(tableName = "messages", primaryKeys = ["id", "senderId", "roomId"])
data class Message(
    @SerializedName("id")
    @Expose
    val id: String = "",

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
    val media: List<String> = emptyList(),

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
    val reactions: List<Reaction> = emptyList(),
){
    var fontSize: Float? = 0f
    var attachments: MutableList<Uri> = mutableListOf<Uri>().apply{
        addAll(media.map { Uri.parse(it) })
    }

    data class Thread(
        @SerializedName("created_at")
        @Expose
        val createdAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 2021-09-18T16:39:54.008Z

        @SerializedName("message_id")
        @Expose
        val messageId: String,

        @SerializedName("sender_id")
        @Expose
        val senderId: String,

        @SerializedName("media")
        @Expose
        val media: List<String> = emptyList(),

        @SerializedName("message")
        @Expose
        val message: String
    )

    data class Reaction(
        @SerializedName("aliases")
        @Expose
        val aliases: List<String>,

        @SerializedName("category")
        @Expose
        val category: String, // string

        @SerializedName("count")
        @Expose
        val count: Int, // 0

        @SerializedName("created_at")
        @Expose
        val createdAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 2021-09-22T19:12:21.221Z

        @SerializedName("data")
        @Expose
        val `data`: String, // string

        @SerializedName("message_id")
        @Expose
        val messageId: String, // string

        @SerializedName("sender_id")
        @Expose
        val senderId: String // string
    )
}