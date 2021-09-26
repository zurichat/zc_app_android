package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.UserList
import javax.inject.Inject

class UserRepository @Inject constructor(private val usersService: UsersService) {

    private lateinit var userList: UserList

    suspend fun login(loginBody: LoginBody): LoginResponse {
       return usersService.login(loginBody)
    }

    suspend fun getUsers(): UserList {
        if(!this::userList.isInitialized)
            userList = usersService.getUsers(auth)
        return userList
    }
}