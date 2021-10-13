package com.zurichat.app.ui.newchannel

import com.zurichat.app.data.functional.Result
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel

interface CreateChannelRemote {
    suspend fun saveNewChannel(createChannelBodyModel: CreateChannelBodyModel,org_id:String):Result<CreateChannelResponseModel>
}