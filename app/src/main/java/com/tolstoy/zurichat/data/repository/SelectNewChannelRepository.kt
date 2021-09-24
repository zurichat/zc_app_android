package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.functional.Failure
import com.tolstoy.zurichat.data.functional.GetUserResult
import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.di.ChannelRetrofitService
import com.tolstoy.zurichat.models.UserList
import javax.inject.Inject

class SelectNewChannelRepository
@Inject constructor(@ChannelRetrofitService private val retrofitService: RetrofitService) {

    suspend fun getUsers(token: String): GetUserResult<UserList> {
        val res = retrofitService.getUsers(token)
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