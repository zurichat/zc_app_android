package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.RegisterUser
import com.tolstoy.zurichat.models.VerifyEmail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


 //'https://api.zuri.chat/v1/auth/login'

interface RetrofitService {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("users")
    fun register(@Body registerUser: RegisterUser?): Call<RegisterUser?>?

    @POST ("account/verify-account")
    fun verifyEmail(@Body verifyEmail : VerifyEmail?): Call<VerifyEmail?>?

}


