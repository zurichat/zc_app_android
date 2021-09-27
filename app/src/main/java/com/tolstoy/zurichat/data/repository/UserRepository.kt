package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.PassswordRestReponse
import com.tolstoy.zurichat.models.PasswordReset
import com.tolstoy.zurichat.util.AUTH_PREF_KEY
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserRepository @Inject constructor(
    private val usersService: UsersService,
    private val preferences: SharedPreferences,
    private val dao: UserDao
) {

    suspend fun login(loginBody: LoginBody): LoginResponse {
        return usersService.login(loginBody)
    }

     suspend fun passwordReset(passwordReset: PasswordReset): PassswordRestReponse {
        return usersService.passwordreset(passwordReset)

    }

    fun saveUserAuthState(value: Boolean) {
        preferences.edit().putBoolean(AUTH_PREF_KEY, value).apply()
    }

    fun getUserAuthState(): Boolean {
        return preferences.getBoolean(AUTH_PREF_KEY, false)
    }

    suspend fun saveUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            dao.saveUser(user)
        }
    }

    suspend fun getUser(): Flow<UserEntity> {
        return withContext(Dispatchers.IO) {
            flow { emit(dao.getUser()) }
        }
    }
}
