package com.tolstoy.zurichat.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.localSource.AccountsDatabase
import com.tolstoy.zurichat.data.repository.AccountsRepository
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application:Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<User>>
    private val repository: AccountsRepository

    init {
        val accountsDao = AccountsDatabase.getDatabase(application).AccountsDao()
        repository = AccountsRepository(accountsDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

}