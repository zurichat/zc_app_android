package com.zurichat.app.ui.newchannel.usecase

import com.chamsmobile.android.core.functional.base.FlowUseCase
import com.zurichat.app.data.functional.Result
import com.zurichat.app.data.repository.ChannelRepository
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateChannel @Inject constructor(
    private val channelRepository: ChannelRepository,
) : FlowUseCase<CreateChannelBodyModel, CreateChannelResponseModel> {
    override fun invoke(params: CreateChannelBodyModel?): Flow<Result<CreateChannelResponseModel>> {
        return channelRepository.createChannel(createChannelBodyModel = params!!)
    }
}
