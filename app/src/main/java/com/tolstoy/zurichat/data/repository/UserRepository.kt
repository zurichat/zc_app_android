package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import com.tolstoy.zurichat.data.localSource.AppDatabase
import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.util.LOGGING_SHARED_PREFERENCE
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepository @Inject constructor(
    private val retrofitService: RetrofitService,
    private val sharedPreferences: SharedPreferences,
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun login(loginBody: LoginBody): LoginResponse {
        return retrofitService.login(loginBody)
    }

    fun getLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGING_SHARED_PREFERENCE, false)
    }

    fun setLoggedIn(value: Boolean) {
        sharedPreferences.edit().putBoolean(LOGGING_SHARED_PREFERENCE, value).apply()
    }

    suspend fun saveUser(userEntity: UserEntity): Flow<Unit> {
        return withContext(dispatcher) {
            flow { emit(database.userDao().saveUser(userEntity)) }
        }
    }

    suspend fun getUserData(): Flow<UserEntity> {
        return withContext(dispatcher) {
            database.userDao().getUser()
        }
    }
}