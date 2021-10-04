package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 10/2/2021 at 9:44 PM
 */
interface DMService {
    @GET("${DM_API}{org_id}/rooms/{room_id}/messages")
    fun getMessages(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String
    ): Call<GetMessagesResponse>

    @GET("dmgetmessage/{room_id}/{message_id}")
    fun getMessage(
        @Path("room_id") roomId: String,
        @Path("message_id") messageId: String
    ): Call<Message>

    @POST("${DM_API}{org_id}/rooms/{room_id}/messages")
    fun sendMessage(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String,
        @Body message: Message
    ): Call<SendMessageResponse>

    @GET("${DM_API}{org_id}/users/{user_id}/rooms")
    fun getRooms(
        @Path("org_id") orgId: String,
        @Path("user_id") userId: String
    ): Call<List<Room>>

    @GET("${DM_API}{org_id}/rooms/{room_id}/info")
    fun getRoomInfo(
        @Path("org_id") orgId: String,
        @Query("room_id") roomId: String
    ): Call<RoomInfoResponse>

    @POST("${DM_API}{org_id}/user/{user_id}/room")
    fun createRoom(
        @Path("org_id") orgId: String,
        @Path("user_id") userId: String,
        @Body roomInfo: CreateRoom
    ): Call<CreateRoomResponse>

    companion object {
        const val BASE_URL = "https://dm.zuri.chat/"
        const val DM_API = "api/v1/org/"
    }
}