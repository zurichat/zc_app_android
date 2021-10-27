package com.zurichat.app.data.repository

import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.zurichat.app.data.localSource.dao.AccountsDao
import com.zurichat.app.data.localSource.dao.StarredMessagesDao
import com.zurichat.app.data.localSource.dao.UserDao
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.data.remoteSource.result
import com.zurichat.app.models.*
import com.zurichat.app.models.network_response.CreateRoom
import com.zurichat.app.ui.login.password.confirm.ConfirmPasswordData
import com.zurichat.app.ui.login.password.confirm.ConfirmResponse
import com.zurichat.app.util.AUTH_PREF_KEY
import com.zurichat.app.util.USER_EMAIL
import com.zurichat.app.util.USER_ID
import com.zurichat.app.util.USER_TOKEN
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import retrofit2.Response

class UserRepository @Inject constructor(
    private val usersService: UsersService,
    private val preferences: SharedPreferences,
    private val dao: UserDao,
    private val starredMessagesDao: StarredMessagesDao,
    private val accountsDao: AccountsDao

) {

    suspend fun login(loginBody: LoginBody): LoginResponse {
        return usersService.login(loginBody)
    }

    suspend fun passwordReset(passwordResetBody: PasswordResetBody): Response<PasswordRestReponse> {
        return usersService.passwordReset(passwordResetBody)
    }

    fun saveUserAuthState(value: Boolean) {
        preferences.edit().putBoolean(AUTH_PREF_KEY, value).apply()
    }

    fun getUserAuthState(): Boolean {
        return preferences.getBoolean(AUTH_PREF_KEY, false)
    }

    suspend fun logout(logoutBody: LogoutBody): Response<LogoutResponse> {
        return usersService.logout(logoutBody)
    }

    suspend fun verifyResetCode(resetCodeBody: ResetCodeBody):Response<ResetCodeResponse>{
        return usersService.verifyResetOtp(resetCodeBody)
    }

    suspend fun updatePassword(updatePassBody: UpdatePassBody, verificationCode:String):Response<LogoutResponse>{
        return usersService.updatePass(updatePassBody = updatePassBody,verCode = verificationCode)
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

    fun getUserEmail() = preferences.getString(USER_EMAIL, "")!!

    fun getUserToken() = preferences.getString(USER_TOKEN, "")!!

    suspend fun getUser() = dao.getUser(getUserId())

    suspend fun confirmPassword(confirmPasswordData: ConfirmPasswordData): ConfirmResponse {
        return usersService.confirmpassword(confirmPasswordData)
    }

    //StarredMessages
    val getAllStarredMessages: Flow<List<StarredMessages>> = starredMessagesDao.getStarredMessages()

    suspend fun starMessage(starredMessages: StarredMessages) {
        starredMessagesDao.starMessage(starredMessages)
    }

    suspend fun unStarMessage(starredMessages: StarredMessages) {
        starredMessagesDao.unStarMessage(starredMessages)
    }

    //User AccountFragment
    val readAllData: LiveData<List<User>> = accountsDao.readAllData()

    suspend fun addUser(user: User) {
        accountsDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        accountsDao.updateUser(user)
    }

    fun getCurUser(): LiveData<User?> {
        return accountsDao.getUser()
    }

    suspend fun deleteUser(user: User){
        accountsDao.deleteUser(user)
    }

    //DM Repository
    suspend fun getMessages(orgId: String, roomId: String) =
        usersService.getMessages(orgId, roomId).result()

    suspend fun getMessage(roomId: String, messageId: String) =
        usersService.getMessage(roomId, messageId).result()

    suspend fun sendMessage(orgId: String, roomId: String, message: Message) =
        usersService.sendMessage(orgId, roomId, message).result()

    suspend fun getRooms(orgId: String, userId: String) =
        usersService.getRooms(orgId, userId).result()

    suspend fun getRoomInfo(orgId: String, roomId: String) =
        usersService.getRoomInfo(orgId, roomId).result()

    suspend fun createRoom(orgId: String, userId: String, room: CreateRoom) =
        usersService.createRoom(orgId, userId, room).result()

}
