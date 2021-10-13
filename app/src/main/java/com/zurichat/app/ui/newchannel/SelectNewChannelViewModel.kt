package com.zurichat.app.ui.newchannel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
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
class SelectNewChannelViewModel @Inject constructor(val repository: SelectNewChannelRepository, val app: Application, val preference: SharedPreferences): ViewModel() {
    private val users = MutableStateFlow<SelectNewChannelViewState<List<OrganizationMember>>>(SelectNewChannelViewState.Empty)
    val _users:StateFlow<SelectNewChannelViewState<List<OrganizationMember>>> = users

    private val endPointResult = MutableStateFlow<SelectNewChannelViewState<String>>(SelectNewChannelViewState.Empty)
    val _endPointResult = endPointResult

    var orgID = MutableLiveData<String>()
    var userList = MutableLiveData<List<OrganizationMember>>()

    init {
        /*Invoking the getUsers() function from the repository to fetch data from the local
        database*/

        viewModelScope.launch {
            //remove this later
            orgID.value = "6145eee9285e4a18402074cd"
            repository.getMembers(orgID.value.toString()).collect {
                when (it) {
                    is GetUserResult.Success -> {
                        users.emit(SelectNewChannelViewState.Success(it.data))
                        userList.value = it.data!!
                    }
                    is GetUserResult.Error -> {
                        users.emit(SelectNewChannelViewState.Error("Error in loading data"))
                    }
                }
            }
        }
    }

    fun getListOfUsers(orgID:String) {
        viewModelScope.launch {
            val res = repository.insertUsers("Bearer ${preference.getString("TOKEN", "")}", "6145eee9285e4a18402074cd")
            when(res) {
                is GetUserResult.Success -> {
                    userList.value = res.data.data
                    endPointResult.value = SelectNewChannelViewState.Success("Loading successful")
                }
                is GetUserResult.Error -> {
                    endPointResult.value = SelectNewChannelViewState.Error("Error in Loading Users")
                }
            }

        }
    }

}
