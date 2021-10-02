package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import com.tolstoy.zurichat.data.remoteSource.TokenInterceptor
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.*
import com.tolstoy.zurichat.util.AUTH_PREF_KEY
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import retrofit2.Response

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

    suspend fun logout(): Response<LogoutResponse> {
        return usersService.logout()
    }

    fun saveUserAuthState(value: Boolean) {
        preferences.edit().putBoolean(AUTH_PREF_KEY, value).apply()
    }

    fun getUserAuthState(): Boolean {
        return preferences.getBoolean(AUTH_PREF_KEY, false)
    }

    fun clearUserAuthState() {
        preferences.edit().putBoolean(AUTH_PREF_KEY, false).apply()
    }

    suspend fun saveUser(user: User) = dao.saveUser(user)

    suspend fun getUser() = flow { emit(dao.getUser()) }

    suspend fun getUsers() = flow {
        // retrieve the users from the db first before making a remote call
        emit(dao.getUsers())
        emit(usersService.getUsers(dao.getUser().token))
    }
}
