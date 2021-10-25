package com.zurichat.app.ui.fragments.switch_account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor (application:Application, private val repository:
UserRepository): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
  //  private val repository: AccountsRepository

    init {
        // val accountsDao = AppDatabase.getDatabase(application).AccountsDao()
       // repository = AccountsRepository(accountsDao)
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