package com.zurichat.app.ui.dm_chat.apiservice

import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.response.member.MemberResponse
import com.zurichat.app.ui.dm_chat.model.response.message.GetMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


//https://api.zuri.chat/organizations/614679ee1a5607b13c00bcb7/members/614f093ee35bb73a77bc2bc5

//https://dm.zuri.chat/dmapi/v1/org/614679ee1a5607b13c00bcb7/rooms/6153851a627d729efec46b7a/messages

interface ApiDMService {

    @GET("users/{user_id}/rooms")
    suspend fun getRooms(
        @Path("user_id") userId: String
    ): Response<RoomsListResponse>

    @GET("rooms/{room_id}/messages")
    suspend fun getMessages(
        @Path("room_id") roomId: String
    ): Response<GetMessageResponse>

    @POST("rooms/{room_id}/messages")
    suspend fun sendMessages(
        @Path("room_id") roomId: String,
        @Body messageBody: SendMessageBody
    ): Response<SendMessageResponse>

    @GET("organizations/614679ee1a5607b13c00bcb7/members{mem_id}")
    suspend fun getMember(
        @Path("mem_id") memId: String,
    ): Response<MemberResponse>


}