package com.zurichat.app.ui.fragments.starred_messages

import androidx.lifecycle.*
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.StarredMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredMessagesViewModel @Inject constructor (private val repository: UserRepository) : ViewModel() {

    val allStarredMessages: LiveData<List<StarredMessages>> = repository.getAllStarredMessages.asLiveData()

    fun starMessage(starredMessages: StarredMessages) = viewModelScope.launch {
        repository.starMessage(starredMessages)
    }
}

class StarredMessageViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StarredMessagesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StarredMessagesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}