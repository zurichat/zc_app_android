package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.functional.Failure
import com.tolstoy.zurichat.data.functional.GetUserResult
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.di.ChannelRetrofitService
import com.tolstoy.zurichat.models.UserList
import javax.inject.Inject

class SelectNewChannelRepository
@Inject constructor(@ChannelRetrofitService private val usersService: UsersService) {

    suspend fun getUsers(token: String): GetUserResult<UserList> {
        val res = usersService.getUsers(token)
        return try {

            if(res.isSuccessful) {
                res.body()?.let {
                    GetUserResult.Success(it)
                }?: GetUserResult.Error(Failure.ServerError)
            } else {
                GetUserResult.Error(Failure.ServerError)
            }
        }
        catch (excep: Exception) {
            GetUserResult.Error(Failure.ServerError)
        }
    }
}