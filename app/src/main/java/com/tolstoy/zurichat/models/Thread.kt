package com.tolstoy.zurichat.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.time.LocalDateTime
import java.time.ZoneOffset

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