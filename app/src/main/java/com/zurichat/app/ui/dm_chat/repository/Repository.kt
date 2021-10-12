package com.zurichat.app.ui.dm_chat.repository

import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.dm_chat.apiservice.RetrofitInstance
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.response.member.MemberResponse
import com.zurichat.app.ui.dm_chat.model.response.message.GetMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import retrofit2.Response

class Repository {
    suspend fun getRooms(): Response<RoomsListResponse> {
        return RetrofitInstance.retrofitService.getRooms("614f088ee35bb73a77bc2b70")
    }

    suspend fun getMember(memId: String): Response<MemberResponse> {
        return RetrofitInstance.retrofitService2.getMember(memId)
    }

    suspend fun getMessages(roomId: String): Response<GetMessageResponse> {
        return RetrofitInstance.retrofitService.getMessages(roomId)
    }

    suspend fun sendMessages(roomId: String, messageBody: SendMessageBody): Response<SendMessageResponse> {
        return RetrofitInstance.retrofitService.sendMessages(roomId, messageBody)
    }

    suspend fun getMemberIds(email: String): Response<UserOrganizationModel> {
        return RetrofitInstance.retrofitService2.getMemberIds(email)
    }

}