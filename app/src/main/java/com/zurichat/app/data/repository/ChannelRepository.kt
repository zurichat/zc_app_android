package com.zurichat.app.data.repository

import com.zurichat.app.data.functional.Result
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun createChannel(createChannelBodyModel: CreateChannelBodyModel,org_ID: String): Flow<Result<CreateChannelResponseModel>>
}
