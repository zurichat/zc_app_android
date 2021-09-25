package com.tolstoy.zurichat.ui.fragments.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages
import com.tolstoy.zurichat.ui.fragments.model.Data
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.model.Message
import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.launch

class ChannelMessagesViewModel : ViewModel(){

    private var _allMessages = MutableLiveData<AllChannelMessages>()
    val allMessages : LiveData<AllChannelMessages> get() = _allMessages

    // This function gets called after entering a channel to retrieve all messages
    fun retrieveAllMessages(organizationId : String, channelId : String){
        viewModelScope.launch {
            try {
                val joinedUser = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.retrieveAllMessages(organizationId,channelId)
                joinedUser?.let {
                    _allMessages.value = it
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    // This function gets called to send a message
    fun sendMessages(data : Data,organizationId : String, channelId : String, dataList: List<Data>){
        viewModelScope.launch {
            try {
                val message = Message(data.user_id.toString(),data.content.toString(),data.files,data.event)
                _allMessages.value!!.data = dataList
                val joinedUser = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.sendMessage(organizationId,channelId,message)
                joinedUser?.let {
                    Log.i("List",it.user_id.toString())
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}