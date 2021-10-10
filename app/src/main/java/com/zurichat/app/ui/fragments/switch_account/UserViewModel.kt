package com.zurichat.app.ui.fragments.switch_account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.localSource.AccountsDatabase
import com.zurichat.app.data.repository.AccountsRepository
import com.zurichat.app.models.User
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

    fun getCurUser():LiveData<User?> {
        return repository.getCurUser()
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }
    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

}