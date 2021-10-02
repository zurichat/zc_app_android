package com.tolstoy.zurichat.ui.dm_chat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.ui.dm_chat.model.request.SendMessageBody
import com.tolstoy.zurichat.ui.dm_chat.model.response.member.MemberResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.message.GetMessageResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.message.SendMessageResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponse
import com.tolstoy.zurichat.ui.dm_chat.repository.Repository
import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<RoomsListResponse>> = MutableLiveData()

    val myResponse2: MutableLiveData<Response<MemberResponse>> = MutableLiveData()

    val myGetMessageResponse: MutableLiveData<Response<GetMessageResponse>> = MutableLiveData()
    val mySendMessageResponse: MutableLiveData<Response<SendMessageResponse>> = MutableLiveData()


    fun getRooms() {
        viewModelScope.launch {
            try {
                val response = repository.getRooms()
                myResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch {
            try {
                val response = repository.getMessages()
                myGetMessageResponse.value = response
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun sendMessages(messageBody: SendMessageBody) {
        viewModelScope.launch {
            try {
                val response = repository.sendMessages(messageBody)
                mySendMessageResponse.value = response
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