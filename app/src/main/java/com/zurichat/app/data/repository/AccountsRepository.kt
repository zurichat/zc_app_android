package com.zurichat.app.data.repository

import androidx.lifecycle.LiveData
import com.zurichat.app.data.localSource.dao.AccountsDao
import com.zurichat.app.models.User

class AccountsRepository(private val accountsDao: AccountsDao) {
    val readAllData: LiveData<List<User>> = accountsDao.readAllData()

    suspend fun addUser(user: User) {
        accountsDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        accountsDao.updateUser(user)
    }

    fun getCurUser(): LiveData<User?> {
        return accountsDao.getUser()
    }

    suspend fun deleteUser(user: User){
        accountsDao.deleteUser(user)
    }

}