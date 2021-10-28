package com.zurichat.app.ui.dm_chat.apiservice

import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.request.createroom.CreateRoomBody
import com.zurichat.app.ui.dm_chat.model.response.createroom.CreateRoomsResponse
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

//https://dm.zuri.chat/api/v1/org/6162210d8e856323d6f12110/users/6162210d8e856323d6f12111/room

interface ApiDMService {

    @GET("org/{org_id}/users/{user_id}/rooms")
    suspend fun getRooms(
        @Path("org_id") orgId: String,
        @Path("user_id") userId: String
    ): Response<RoomsListResponse>

    @GET("org/{org_id}/rooms/{room_id}/messages")
    suspend fun getMessages(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String
    ): Response<GetMessageResponse>

    @POST("org/{org_id}/rooms/{room_id}/messages")
    suspend fun sendMessages(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String,
        @Body messageBody: SendMessageBody
    ): Response<SendMessageResponse>

    @GET("organizations/614679ee1a5607b13c00bcb7/members{mem_id}")
    suspend fun getMember(
        @Path("mem_id") memId: String
    ): Response<MemberResponse>

    @GET("users/{email}/organizations")
    suspend fun getMemberIds(
        @Path("email") email: String
    ): Response<UserOrganizationModel>

    @POST("users/{mem_id}/room")
    suspend fun createRoom(
        @Path("mem_id") memId: String,
        @Body createRoomBody: CreateRoomBody
    ): Response<CreateRoomsResponse>

    companion object {
        const val BASE_URL = "https://dm.zuri.chat/api/v1/"
        const val DM_API = "api/v1/org/"
    }
}