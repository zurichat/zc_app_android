package com.tolstoy.zurichat.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
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
}