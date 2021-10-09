package com.zurichat.app.ui.newchannel.states

import com.zurichat.app.models.CreateChannelResponseModel

sealed class CreateChannelViewState {
    class Success(
        val message: Int,
        val createChannelResponseModel: CreateChannelResponseModel? = null,
    ) : CreateChannelViewState()

    class Failure(val message: Int) : CreateChannelViewState()
    class Loading(val message: Int? = null) : CreateChannelViewState()
    object Empty : CreateChannelViewState()
}
