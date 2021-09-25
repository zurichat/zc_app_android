package com.tolstoy.zurichat.ui.newchannel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.functional.GetUserResult
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.data.repository.SelectNewChannelRepository
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.UserList
import com.tolstoy.zurichat.ui.newchannel.fragment.SelectNewChannelFragment
import com.tolstoy.zurichat.ui.newchannel.states.SelectNewChannelViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KProperty

@HiltViewModel
class SelectNewChannelViewModel @Inject constructor
    (val repository: SelectNewChannelRepository, val app: Application): ViewModel() {

    private val users = MutableStateFlow<SelectNewChannelViewState<UserList>>(SelectNewChannelViewState.Empty)
    val _users:StateFlow<SelectNewChannelViewState<UserList>> = users
    private val token = app.applicationContext.getSharedPreferences("LOGIN_TOKEN", Context.MODE_PRIVATE)

    fun getListOfUsers() {
        viewModelScope.launch {
            val res = repository.getUsers("Bearer ${token.getString("TOKEN", "")}")

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