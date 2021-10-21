package com.zurichat.app.ui.dm_chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.localSource.dm.RoomMessageDao
import com.zurichat.app.data.localSource.dm.RoomModel
import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.request.createroom.CreateRoomBody
import com.zurichat.app.ui.dm_chat.model.response.createroom.CreateRoomsResponse
import com.zurichat.app.ui.dm_chat.model.response.member.MemberResponse
import com.zurichat.app.ui.dm_chat.model.response.message.GetMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import com.zurichat.app.ui.dm_chat.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<RoomsListResponse>> = MutableLiveData()

    val myResponse2: MutableLiveData<Response<MemberResponse>> = MutableLiveData()

    val myGetMessageResponse: MutableLiveData<Response<GetMessageResponse>> = MutableLiveData()
    val mySendMessageResponse: MutableLiveData<Response<SendMessageResponse>> = MutableLiveData()
    val myMemberIdsResponse: MutableLiveData<Response<UserOrganizationModel>> = MutableLiveData()
    val myCreateRoomResponse: MutableLiveData<Response<CreateRoomsResponse>> = MutableLiveData()

//    private var _roomLiveData: MutableLiveData<List<RoomModel>> = MutableLiveData()
//    val roomLiveData get() = _roomLiveData


    fun getRooms(orgId: String, memId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getRooms(orgId, memId)
                myResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun createRoom(memId: String, createRoomBody: CreateRoomBody) {
        viewModelScope.launch {
            try {
                val response = repository.createRoom(memId, createRoomBody)
                myCreateRoomResponse.value = response
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMessages(orgId: String, roomId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMessages(orgId, roomId)
                myGetMessageResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun sendMessages(orgId: String, roomId: String, messageBody: SendMessageBody) {
        viewModelScope.launch {
            try {
                val response = repository.sendMessages(orgId, roomId, messageBody)
                mySendMessageResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMemberIds(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMemberIds(email)
                myMemberIdsResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMember(memID: String) {
        try {
            viewModelScope.launch {
                val response = repository.getMember(memID)
                myResponse2.value = response
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


}