package com.zurichat.app.ui.newchannel

import android.app.Application
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.functional.GetUserResult
import com.zurichat.app.data.repository.*
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.Room
import com.zurichat.app.models.User
import com.zurichat.app.models.network_response.CreateRoom
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.newchannel.states.SelectNewChannelViewState
import com.zurichat.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectNewChannelViewModel @Inject constructor(
    val repository: SelectNewChannelRepository,
    private val orgRepo: OrganizationRepository,
    private val dmRepository: DMRepository,
    private val userRepository: UserRepository,
    val app: Application,
    val preference: SharedPreferences): ViewModel() {

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
            //orgID.value = "6145eee9285e4a18402074cd"
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
            val res = repository.insertUsers("Bearer ${preference.getString("TOKEN", "")}", orgID)
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

    fun getId() = orgRepo.getId()

    suspend fun createRoom(member: OrganizationMember) = viewModelScope.async {
        val room = CreateRoom(getId(), listOf(orgRepo.getMemberId(), member.id), member.name())
        val result = dmRepository.createRoom(orgRepo.getId(), orgRepo.getMemberId(), room)
        return@async if(result is Result.Success) {
            Result.Success(bundleOf(
                "room" to RoomsListResponseItem(result.data!!.roomId, org_id = room.orgId,
                    room_user_ids = room.roomMemberIds, room_name = member.name()),
                "USER" to userRepository.getUser()
            ))
        } else result as Result.Error
    }.await()

}
