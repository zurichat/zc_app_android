package com.tolstoy.zurichat.data.repository

import androidx.lifecycle.LiveData
import com.tolstoy.zurichat.data.localSource.dao.AccountsDao
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.models.User

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


}