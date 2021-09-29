package com.tolstoy.zurichat.ui.newchannel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.functional.GetUserResult
import com.tolstoy.zurichat.data.repository.SelectNewChannelRepository
import com.tolstoy.zurichat.models.UserList
import com.tolstoy.zurichat.ui.newchannel.states.SelectNewChannelViewState
import com.tolstoy.zurichat.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectNewChannelViewModel @Inject
constructor(val repository: SelectNewChannelRepository,
            val sharedPreferences: SharedPreferences,
            val app: Application): ViewModel() {

    private val users = MutableStateFlow<SelectNewChannelViewState<UserList>>(SelectNewChannelViewState.Empty)
    val _users:StateFlow<SelectNewChannelViewState<UserList>> = users

    fun getListOfUsers() {
        viewModelScope.launch {
            val res = repository.getUsers("Bearer ${sharedPreferences.getString(USER_TOKEN, "")}")

            when(res) {
                is GetUserResult.Success -> {
                    users.value = SelectNewChannelViewState.Success(res.data)
                }
                is GetUserResult.Error -> {
                    users.value = SelectNewChannelViewState.Error("Error in Loading Users")
                }
            }

        }
    }


}