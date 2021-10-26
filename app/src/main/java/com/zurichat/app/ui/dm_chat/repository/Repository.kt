package com.zurichat.app.ui.dm_chat.repository

import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.dm_chat.apiservice.ApiDMService
import com.zurichat.app.ui.dm_chat.apiservice.RetrofitInstance
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.request.createroom.CreateRoomBody
import com.zurichat.app.ui.dm_chat.model.response.createroom.CreateRoomsResponse
import com.zurichat.app.ui.dm_chat.model.response.member.MemberResponse
import com.zurichat.app.ui.dm_chat.model.response.message.GetMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val roomService: ApiDMService) {

    suspend fun getRooms(orgId: String, memId: String): Response<RoomsListResponse> {
        return RetrofitInstance.retrofitService.getRooms(orgId, memId)
    }

    suspend fun getMember(memId: String): Response<MemberResponse> {
        return RetrofitInstance.retrofitService2.getMember(memId)
    }

    suspend fun getMessages(orgId: String,roomId: String): Response<GetMessageResponse> {
        return roomService.getMessages(orgId, roomId)
    }

    suspend fun sendMessages(orgId: String, roomId: String, messageBody: SendMessageBody): Response<SendMessageResponse> {
        return roomService.sendMessages(orgId, roomId, messageBody)
    }

    suspend fun getMemberIds(email: String): Response<UserOrganizationModel> {
        return RetrofitInstance.retrofitService2.getMemberIds(email)
    }
    suspend fun createRoom(memId: String, createRoomBody: CreateRoomBody): Response<CreateRoomsResponse> {
        return RetrofitInstance.retrofitService.createRoom(memId, createRoomBody)
    }

}