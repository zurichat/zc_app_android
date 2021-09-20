package com.tolstoy.zurichat.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
data class MessageLinkResponse(
    @SerializedName("room_id")
    @Expose
    val roomId: String,

    @SerializedName("message_id")
    @Expose
    val messageId: String,

    @SerializedName("link")
    @Expose
    val link: String
)