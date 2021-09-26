package com.tolstoy.zurichat.ui.newchannel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.UserList

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
                .getUsers(auth = "Bearer ${token.getString("TOKEN","")}")

            emit(usersData.data)

        }




    private fun getRetrofitService(url: String): UsersService {
        return RetrofitChannelClient.getRetrofit(url)
            .create(UsersService::class.java)
    }
}