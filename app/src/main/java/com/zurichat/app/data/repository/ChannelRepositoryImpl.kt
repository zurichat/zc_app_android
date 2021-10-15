package com.zurichat.app.data.repository


import com.zurichat.app.data.functional.Result
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import com.zurichat.app.ui.newchannel.CreateChannelRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(private val createChannelRemote: CreateChannelRemote) : ChannelRepository {
    override fun createChannel(createChannelBodyModel: CreateChannelBodyModel,org_ID: String): Flow<Result<CreateChannelResponseModel>> {
        return flow {
            when (val res = createChannelRemote.saveNewChannel(createChannelBodyModel,org_ID)) {
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
