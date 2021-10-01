package com.tolstoy.zurichat.ui.dm_chat.apiservice

import com.tolstoy.zurichat.ui.dm_chat.model.response.member.MemberResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


//https://api.zuri.chat/organizations/614679ee1a5607b13c00bcb7/members/614f093ee35bb73a77bc2bc5

interface ApiDMService {

    @GET("users/{user_id}/rooms")
    suspend fun getRooms(
        @Path("user_id") userId: String
    ): Response<RoomsListResponse>

    @GET("organizations/614679ee1a5607b13c00bcb7/members{mem_id}")
    suspend fun getMember(
        @Path("mem_id") memId: String
    ): Response<MemberResponse>
}