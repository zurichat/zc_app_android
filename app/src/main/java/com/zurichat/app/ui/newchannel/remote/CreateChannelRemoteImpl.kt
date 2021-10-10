package com.zurichat.app.ui.newchannel.remote

import com.zurichat.app.data.functional.Failure
import com.zurichat.app.data.functional.Result
import com.zurichat.app.data.remoteSource.NewChannelApiService
import com.zurichat.app.data.remoteSource.ResponseCode
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import com.zurichat.app.ui.newchannel.CreateChannelRemote
import javax.inject.Inject

class CreateChannelRemoteImpl @Inject constructor(
    private val apiService: NewChannelApiService,
) : CreateChannelRemote {
    override suspend fun saveNewChannel(createChannelBodyModel: CreateChannelBodyModel): Result<CreateChannelResponseModel> {
        return try {
            val res = apiService.createChannel(createChannelBodyModel = createChannelBodyModel)
            when (res.isSuccessful) {
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false -> {
                    when (res.code()) {
                        ResponseCode.INVALID_PARAMETER -> {
                            Result.Error(Failure.InvalidParameter)

                        }
                        ResponseCode.USER_NOT_FOUND -> {
                            Result.Error(Failure.userNotFound)

                        }
                        else -> {
                            Result.Error(Failure.ServerError)
                        }
                    }

                }
            }
        } catch (exc: Exception) {
            println(exc)
            Result.Error(Failure.ServerError)
        }
    }


}

