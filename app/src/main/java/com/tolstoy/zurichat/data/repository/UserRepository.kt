package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.util.LOGGING_SHAREDPREFERENCE
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val retrofitService: RetrofitService,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun login(loginBody: LoginBody): LoginResponse {
        return retrofitService.login(loginBody)
    }

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(LOGGING_SHAREDPREFERENCE, false)
        set(value) = sharedPreferences.edit().putBoolean(LOGGING_SHAREDPREFERENCE, value).apply()
}