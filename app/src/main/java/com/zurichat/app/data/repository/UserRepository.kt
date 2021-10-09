package com.zurichat.app.data.repository

import android.content.SharedPreferences
import com.zurichat.app.data.localSource.dao.UserDao
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.models.*
import com.zurichat.app.ui.login.password.confirm.ConfirmPasswordData
import com.zurichat.app.ui.login.password.confirm.ConfirmResponse
import com.zurichat.app.ui.login.password.resetuserpass.ResetUserPasswordData
import com.zurichat.app.ui.login.password.resetuserpass.ResetUserPasswordResponse
import com.zurichat.app.util.AUTH_PREF_KEY
import com.zurichat.app.util.USER_EMAIL
import com.zurichat.app.util.USER_ID
import com.zurichat.app.util.USER_TOKEN
import javax.inject.Inject
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
        return usersService.passwordReset(passwordReset)
    }

    fun saveUserAuthState(value: Boolean) {
        preferences.edit().putBoolean(AUTH_PREF_KEY, value).apply()
    }

    fun getUserAuthState(): Boolean {
        return preferences.getBoolean(AUTH_PREF_KEY, false)
    }

    suspend fun logout(): Response<LogoutResponse> {
        return usersService.logout()
    }

    fun clearUserAuthState() {
        preferences.edit().putBoolean(AUTH_PREF_KEY, false).apply()
    }

    suspend fun saveUser(user: User) {
        // save the user in the db
        dao.saveUser(user)
        // save some important details from the user that are going to be used throughout the app
        preferences.edit()
            .putString(USER_TOKEN, user.token)
            .putString(USER_ID, user.id)
            .putString(USER_EMAIL, user.email)
            .apply()
    }

    fun getUserId() = preferences.getString(USER_ID, "")!!

    fun getUserToken() = preferences.getString(USER_TOKEN, "")!!

    suspend fun getUser() = dao.getUser(getUserId())

    suspend fun confirmPassword(confirmPasswordData: ConfirmPasswordData): ConfirmResponse {
        return usersService.confirmpassword(confirmPasswordData)
    }

    suspend fun resetUserPassword( resetUserPasswordData: ResetUserPasswordData): ResetUserPasswordResponse{
        return usersService.resetUserPassword(resetUserPasswordData)
    }
}
