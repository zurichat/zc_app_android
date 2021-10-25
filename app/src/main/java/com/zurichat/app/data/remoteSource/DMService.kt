package com.zurichat.app.data.remoteSource

import com.zurichat.app.models.Message
import com.zurichat.app.models.Room
import com.zurichat.app.models.network_response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 10/2/2021 at 9:44 PM
 */
//interface DMService {
//    @GET("${DM_API}{org_id}/rooms/{room_id}/messages")
//    suspend fun getMessages(
//        @Path("org_id") orgId: String,
//        @Path("room_id") roomId: String
//    ): Response<GetMessagesResponse>
//
//
//    @GET("dmgetmessage/{room_id}/{message_id}")
//    suspend fun getMessage(
//        @Path("room_id") roomId: String,
//        @Path("message_id") messageId: String
//    ): Response<Message>
//    @POST("${DM_API}{org_id}/rooms/{room_id}/messages")
//    suspend fun sendMessage(
//        @Path("org_id") orgId: String,
//        @Path("room_id") roomId: String,
//        @Body message: Message
//    ): Response<SendMessageResponse>
//
//    @GET("${DM_API}{org_id}/users/{user_id}/rooms")
//    suspend fun getRooms(
//        @Path("org_id") orgId: String,
//        @Path("user_id") userId: String
//    ): Response<List<Room>>
//
//    @GET("${DM_API}{org_id}/rooms/{room_id}/info")
//    suspend fun getRoomInfo(
//        @Path("org_id") orgId: String,
//        @Query("room_id") roomId: String
//    ): Response<RoomInfoResponse>
//
//    @POST("${DM_API}{org_id}/users/{user_id}/room")
//    suspend fun createRoom(
//        @Path("org_id") orgId: String,
//        @Path("user_id") userId: String,
//        @Body roomInfo: CreateRoom
//    ): Response<CreateRoomResponse>
//
//    companion object {
//        const val BASE_URL = "https://dm.zuri.chat/"
//        const val DM_API = "api/v1/org/"
//    }
//}