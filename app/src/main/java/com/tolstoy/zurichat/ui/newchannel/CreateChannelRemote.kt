package com.tolstoy.zurichat.ui.newchannel

import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel

interface CreateChannelRemote {
    suspend fun saveNewChannel(createChannelBodyModel: CreateChannelBodyModel):Result<CreateChannelResponseModel>
}