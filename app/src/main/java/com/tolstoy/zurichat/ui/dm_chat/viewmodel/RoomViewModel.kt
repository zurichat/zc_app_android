package com.tolstoy.zurichat.ui.dm_chat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.ui.dm_chat.model.response.member.MemberResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponse
import com.tolstoy.zurichat.ui.dm_chat.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<RoomsListResponse>> = MutableLiveData()

    val myResponse2: MutableLiveData<Response<MemberResponse>> = MutableLiveData()

    fun getRooms() {

        viewModelScope.launch {
            val response = repository.getRooms()
            myResponse.value = response
        }
    }

    fun getMember(memID: String) {

        viewModelScope.launch {
            val response = repository.getMember(memID)
            myResponse2.value = response
        }
    }

}