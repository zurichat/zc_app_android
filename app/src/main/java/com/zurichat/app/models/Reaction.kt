package com.zurichat.app.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.time.LocalDateTime
import java.time.ZoneOffset

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