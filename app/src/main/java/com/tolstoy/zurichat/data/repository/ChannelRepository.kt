package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun createChannel(createChannelBodyModel: CreateChannelBodyModel): Flow<Result<CreateChannelResponseModel>>
}
