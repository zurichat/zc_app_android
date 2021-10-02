package com.tolstoy.zurichat.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.ui.fragments.model.Data
import kotlinx.coroutines.launch

class SharedChannelViewModel : ViewModel(){
    private var _newMessage = MutableLiveData<Data>()
    val newMessage : LiveData<Data> get() = _newMessage

    // This function gets called after a new message enters
    fun receiveNewMessage(data : Data){
        viewModelScope.launch {
            _newMessage.value = data
        }
    }
}