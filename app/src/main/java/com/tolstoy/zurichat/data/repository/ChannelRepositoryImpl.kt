package com.tolstoy.zurichat.data.repository


import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel
import com.tolstoy.zurichat.ui.newchannel.CreateChannelRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(private val createChannelRemote: CreateChannelRemote) :
    ChannelRepository {
    override fun createChannel(createChannelBodyModel: CreateChannelBodyModel): Flow<Result<CreateChannelResponseModel>> {
        return flow {
            when (val res = createChannelRemote.saveNewChannel(createChannelBodyModel)) {
                is Result.Success -> {
                    emit(Result.Success(res.data))
                }
                is Result.Error -> {
                    emit(Result.Error(res.failure))
                }
            }
        }
    }
}
