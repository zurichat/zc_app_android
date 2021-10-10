package com.zurichat.app.ui.fragments.starred_messages

import androidx.lifecycle.*
import com.zurichat.app.data.repository.StarredMessagesRepository
import com.zurichat.app.models.StarredMessages
import kotlinx.coroutines.launch

class StarredMessagesViewModel(private val repository: StarredMessagesRepository) : ViewModel() {

    val allStarredMessages: LiveData<List<StarredMessages>> = repository.getAllStarredMessages.asLiveData()

    fun starMessage(starredMessages: StarredMessages) = viewModelScope.launch {
        repository.starMessage(starredMessages)
    }
}

class StarredMessageViewModelFactory(private val repository: StarredMessagesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StarredMessagesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StarredMessagesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}