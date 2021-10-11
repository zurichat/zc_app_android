package com.zurichat.app.ui.newchannel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.R
import com.zurichat.app.data.functional.Failure
import com.zurichat.app.data.functional.Result
import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.ui.newchannel.states.CreateChannelViewState
import com.zurichat.app.ui.newchannel.usecase.CreateChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateChannelViewModel @Inject constructor(
    private val createChannelRemote: CreateChannel,
) : ViewModel() {

    private val _createChannelFlow =
        MutableStateFlow<CreateChannelViewState>(CreateChannelViewState.Empty)
    val createChannelViewFlow: StateFlow<CreateChannelViewState> = _createChannelFlow

    fun createNewChannel(createChannelBodyModel: CreateChannelBodyModel) {
        viewModelScope.launch {
            createChannelRemote(createChannelBodyModel).collect {
                when (it) {
                    is Result.Success -> {
                        _createChannelFlow.value =
                            CreateChannelViewState.Success(R.string.successful_new_channel_creation, it.data)
                    }
                    is Result.Error -> {
                        when (it.failure) {
                            Failure.InvalidParameter -> {
                                _createChannelFlow.value =
                                    CreateChannelViewState.Failure(R.string.invalid_information)
                            }   Failure.userNotFound -> {
                                _createChannelFlow.value =
                                    CreateChannelViewState.Failure(R.string.user_not_found)
                            }
                            else -> {
                                _createChannelFlow.value =
                                    CreateChannelViewState.Failure(R.string.unable_to_create_new_channel)
                            }
                        }
                    }
                }
            }
        }

    }


}
