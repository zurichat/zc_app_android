package com.tolstoy.zurichat.ui.newchannel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.models.UserList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SelectNewChannelViewModel(val app: Application): AndroidViewModel(app) {

    private val loginUrl = "https://api.zuri.chat/"
    private val usersUrl = "https://api.zuri.chat/"
    lateinit var response: LoginResponse
    lateinit var usersData: UserList
    val token = app.applicationContext.getSharedPreferences("LOGIN_TOKEN", Context.MODE_PRIVATE)

    fun getListOfUsers() =
        liveData {
            /*response =  getRetrofitService(loginUrl)
                .login(LoginBody("woleconcept@gmail.com","slimbryan"))*/

            usersData = getRetrofitService(usersUrl)
                .getUsers(token = "Bearer ${token.getString("TOKEN","")}")

            emit(usersData.data)

        }

    private fun getRetrofitService(url: String): RetrofitService {
        return RetrofitChannelClient.getRetrofit(url)
            .create(RetrofitService::class.java)
    }
}