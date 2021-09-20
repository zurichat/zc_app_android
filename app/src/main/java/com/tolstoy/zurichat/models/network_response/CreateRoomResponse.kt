package com.tolstoy.zurichat.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
data class CreateRoomResponse(
    @SerializedName("room_id")
    @Expose
    val roomId: String
)
