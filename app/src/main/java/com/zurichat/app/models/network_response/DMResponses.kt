package com.zurichat.app.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zurichat.app.models.Message

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 22-Sep-21
 */

data class GetMessagesResponse(
    @SerializedName("count")
    @Expose
    val count: Int,
    val next: Any,
    val previous: Any,
    @SerializedName("results")
    @Expose
    val messages: List<Message>,
)

data class SendMessageResponse(
    @SerializedName("status")
    @Expose
    val status: String = "", // success
    @SerializedName("event")
    @Expose
    val event: String = "", // message_create
    @SerializedName("message_id")
    @Expose
    val messageId: String = "", // 614b878a44a9bd81cedc0ce3
    @SerializedName("room_id")
    @Expose
    val roomId: String = "", // 614b774544a9bd81cedc0cbb
    @SerializedName("thread")
    @Expose
    val thread: Boolean = false, // false
    @SerializedName("data")
    @Expose
    val `data`: SendMessageData = SendMessageData()
){
    class SendMessageData(
        @SerializedName("sender_id")
        @Expose
        val senderId: String = "", // 6146464b1a5607b13c00bc25
        @SerializedName("message")
        @Expose
        val message: String = "", // Hello
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "" // 2021-09-22T18:40:48.977000Z
 )
}

data class CreateRoomResponse(
    @SerializedName("room_id")
    @Expose
    val roomId: String = "", // 614b774544a9bd81cedc0cbb
)