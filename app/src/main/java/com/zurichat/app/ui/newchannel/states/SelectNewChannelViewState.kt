package com.zurichat.app.ui.newchannel.states

sealed class SelectNewChannelViewState<out T> {
    data class Success<R>(val data: R): SelectNewChannelViewState<R>()
    data class Error(val error: String): SelectNewChannelViewState<Nothing>()
    object Empty: SelectNewChannelViewState<Nothing>()
}
