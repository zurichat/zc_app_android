package com.zurichat.app.ui.newchannel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.functional.GetUserResult
import com.zurichat.app.data.repository.SelectNewChannelRepository
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.ui.newchannel.states.SelectNewChannelViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectNewChannelViewModel @Inject constructor
    (val repository: SelectNewChannelRepository,
     val app: Application,
     val preference: SharedPreferences): ViewModel() {

    private val users = MutableStateFlow<SelectNewChannelViewState<List<OrganizationMember>>>(SelectNewChannelViewState.Empty)
    val _users:StateFlow<SelectNewChannelViewState<List<OrganizationMember>>> = users

    private val endPointResult = MutableStateFlow<SelectNewChannelViewState<String>>(SelectNewChannelViewState.Empty)
    val _endPointResult = endPointResult

    init {
        /*Invoking the getUsers() function from the repository to fetch data from the local
        database*/

        viewModelScope.launch {
            repository.getMembers("6145eee9285e4a18402074cd").collect {
                when (it) {
                    is GetUserResult.Success -> {
                        users.emit(SelectNewChannelViewState.Success(it.data))
                    }
                    is GetUserResult.Error -> {
                        users.emit(SelectNewChannelViewState.Error("Error in loading data"))
                    }
                }
            }
        }
    }


    /*Invoking the insertUsers() function from the repository to fetch users from the endpoint,
    and insert into the local database*/

    fun getListOfUsers() {
        viewModelScope.launch {
            val res = repository.insertUsers("Bearer ${preference.getString("TOKEN", "")}",
                "6145eee9285e4a18402074cd")

            when(res) {
                is GetUserResult.Success -> {
                    endPointResult.value = SelectNewChannelViewState.Success("Loading successful")
                }
                is GetUserResult.Error -> {
                    endPointResult.value = SelectNewChannelViewState.Error("Error in Loading Users")
                }
            }

        }
    }

}
