package com.tolstoy.zurichat.ui.newchannel.remote

import com.tolstoy.zurichat.data.functional.Failure
import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.data.remoteSource.ResponseCode
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel
import com.tolstoy.zurichat.ui.newchannel.CreateChannelRemote
import javax.inject.Inject

class CreateChannelRemoteImpl @Inject constructor(
    private val apiService: RetrofitService
): CreateChannelRemote {
    override suspend fun saveNewChannel(createChannelBodyModel: CreateChannelBodyModel): Result<CreateChannelResponseModel> {
        return try {
            val res = apiService.createChannel(createChannelBodyModel = createChannelBodyModel)
            when(res.isSuccessful){
                true ->{
                    res.body()?.let {
                        Result.Success(it)
                    }?:Result.Error(Failure.ServerError)
                }
                false->{
                    when(res.code()){
                        ResponseCode.INVALID_PARAMETER->{
                           Result.Error(Failure.InvalidParameter)
                        }else->{
                        Result.Error(Failure.ServerError)
                    }
                    }

                }
            }
        }catch (exc:Exception){
            Result.Error(Failure.ServerError)
        }
    }


}

