package com.tolstoy.zurichat.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.model.*
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages
import com.tolstoy.zurichat.ui.fragments.model.Data
import com.tolstoy.zurichat.ui.fragments.model.Message
import com.tolstoy.zurichat.ui.fragments.model.RoomData
import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.launch

class ChannelMessagesViewModel : ViewModel(){
    private var _allMessages = MutableLiveData<AllChannelMessages>()
    val allMessages : LiveData<AllChannelMessages> get() = _allMessages


    private var _channelData = MutableLiveData<ChannelData>()
    val channelData: LiveData<ChannelData> get() = _channelData


    private var _roomData = MutableLiveData<RoomData>()
    val roomData : LiveData<RoomData> get() = _roomData

    private var _newMessage = MutableLiveData<Data>()
    val newMessage : LiveData<Data> get() = _newMessage

    private var _connected = MutableLiveData<Boolean>()
    val connected : LiveData<Boolean> get() = _connected

    // This function gets called after entering a channel to retrieve all messages
    fun retrieveAllMessages(organizationId: String, channelId: String) {
        viewModelScope.launch {
            try {
                val joinedUser =
                    RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.retrieveAllMessages(organizationId, channelId)
                joinedUser?.let {
                    _allMessages.value = AllChannelMessages(it,"",200)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // This function gets called to send a message
    fun sendMessages(data: Data, organizationId: String, channelId: String, dataList: List<Data>) {
        viewModelScope.launch {
            try {
                var allMessagesNew = AllChannelMessages(dataList,"",200)
                if (_allMessages.value != null){
                    allMessagesNew = _allMessages.value!!
                }

                val mutableDataList = dataList.toMutableList()
                mutableDataList.add(data)
                val position = mutableDataList.indexOf(data)

                //Add Message With Temporary ID To List And Notifies The ViewModel
                allMessagesNew.data = mutableDataList
                _allMessages.value = allMessagesNew

                //_newMessage.value = data
               // val message = Message(data.user_id.toString(),data.content.toString(),data.files,data.event)
               // val joinedUser = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.sendMessage(organizationId,channelId,message)
                val message = Message(data.user_id.toString(),
                    data.content.toString(),
                    data.files,
                    data.event)
                _allMessages.value!!.data = dataList
                val joinedUser =
                    RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)
                        ?.sendMessage(organizationId, channelId, message)
                joinedUser?.let {
                    //_newMessage.value = data

                    //Replaces The Message Item with Message Item with Permanent ID gotten from server After Sending THe Message
                    mutableDataList[position] = it
                    allMessagesNew.data = mutableDataList
                    _allMessages.value = allMessagesNew
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun sendMessages(data : Data){
        viewModelScope.launch {

        }
    }

    // This function gets called after a new message enters
    fun receiveMessage(data : Data){
        viewModelScope.launch {
            _newMessage.value = data
        }
    }

    // This function gets called after entering a channel to get the Centrifugo socket is
    fun retrieveRoomData(organizationId : String, channelId : String){
        viewModelScope.launch {
            try {
                val room = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.getRoom(organizationId,channelId)
                room?.let {
                    _roomData.value = it
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun isConnected(isConnected : Boolean){
        _connected.value = isConnected
    }

    fun saveChannel(channelData: ChannelData) {
        _channelData.value = channelData
    }
}