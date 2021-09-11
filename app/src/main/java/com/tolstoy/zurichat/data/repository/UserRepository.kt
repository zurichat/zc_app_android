package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import javax.inject.Inject

class UserRepository @Inject constructor(private val retrofitService: RetrofitService) {

    suspend fun login(loginBody: LoginBody): LoginResponse {
       return retrofitService.login(loginBody)
    }
}