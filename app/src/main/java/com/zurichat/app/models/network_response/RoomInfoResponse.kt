package com.zurichat.app.models.network_response


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class RoomInfoResponse(
    @SerializedName("created_at")
    @Expose
    val createdAt: String, // 2021-09-18T16:52:23.204Z
    @SerializedName("description")
    @Expose
    val description: String, // string
    @SerializedName("_id")
    @Expose
    val id: String, // string
    @SerializedName("org_id")
    @Expose
    val orgId: String, // string
    @SerializedName("room_user_ids")
    @Expose
    val roomUserIds: List<String>
)