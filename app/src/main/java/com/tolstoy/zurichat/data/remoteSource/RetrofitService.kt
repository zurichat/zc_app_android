package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


 //'https://api.zuri.chat/v1/auth/login'

interface RetrofitService {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("users")
    fun register(@Body registerUser: RegisterUser?): Call<RegisterUser?>?

    @POST("v1/1/channels/")
    suspend fun createChannel(@Body createChannelBodyModel: CreateChannelBodyModel): Response<CreateChannelResponseModel>

}


