package com.tolstoy.zurichat.ui.dm_chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tolstoy.zurichat.ui.dm_chat.repository.Repository

class RoomViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RoomViewModel(repository) as T
    }
}