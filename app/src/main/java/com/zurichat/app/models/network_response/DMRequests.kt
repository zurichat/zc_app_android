package com.zurichat.app.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 22-Sep-21
 */

data class CreateRoom(
    @SerializedName("org_id")
    @Expose
    val orgId: String, // 6145eee9285e4a18402074cd
    @SerializedName("room_member_ids")
    @Expose
    val roomMemberIds: List<String>,
    @SerializedName("room_name")
    @Expose
    val roomName: String = roomMemberIds.toString()
)