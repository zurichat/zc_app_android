package com.tolstoy.zurichat.ui.newchannel.usecase

import com.chamsmobile.android.core.functional.base.FlowUseCase
import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.data.repository.ChannelRepository
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateChannel @Inject constructor(
    private val channelRepository: ChannelRepository,
) : FlowUseCase<CreateChannelBodyModel, CreateChannelResponseModel> {
    override fun invoke(params: CreateChannelBodyModel?): Flow<Result<CreateChannelResponseModel>> {
        println("############ $params")
        return channelRepository.createChannel(createChannelBodyModel = params!!)
    }
}
