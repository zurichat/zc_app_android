package com.tolstoy.zurichat.models.network_response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class MessageResponse(
    @SerializedName("created_at")
    @Expose
    val createdAt: String, // 2021-09-18T16:53:33.079Z
    @SerializedName("data")
    @Expose
    val `data`: Data,
    @SerializedName("message_id")
    @Expose
    val messageId: String, // string
    @SerializedName("status")
    @Expose
    val status: String, // string
    @SerializedName("thread")
    @Expose
    val thread: Boolean // true
)