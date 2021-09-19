package com.tolstoy.zurichat.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Thread(
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null, // 2021-09-18T16:39:54.008Z

    @SerializedName("media")
    @Expose
    val media: List<String>? = null,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("message_id")
    @Expose
    val messageId: String,

    @SerializedName("sender_id")
    @Expose
    val senderId: String
)