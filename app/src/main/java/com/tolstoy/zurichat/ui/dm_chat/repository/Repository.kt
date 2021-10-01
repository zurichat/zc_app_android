package com.tolstoy.zurichat.ui.dm_chat.repository

import com.tolstoy.zurichat.ui.dm_chat.apiservice.RetrofitInstance
import com.tolstoy.zurichat.ui.dm_chat.model.response.member.MemberResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponse
import retrofit2.Response

class Repository {
    suspend fun getRooms(): Response<RoomsListResponse> {

        return RetrofitInstance.retrofitService.getRooms("61467ee61a5607b13c00bcf2")
    }

    suspend fun getMember(memId: String): Response<MemberResponse> {
        return RetrofitInstance.retrofitService2.getMember(memId)
    }

}