package com.tolstoy.zurichat.ui.fragments.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.JoinedChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.networking.ChannelsList
import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

/**
 * Viewmodel to handle updates to the list when a network call is made and result is retrieved
 */
class ChannelViewModel : ViewModel() {
    private var _channelsList = MutableLiveData<List<ChannelModel>>()
    val channelsList : LiveData<List<ChannelModel>> get() = _channelsList

    private var _joinedChannelsList = MutableLiveData<List<JoinedChannelModel>>()
    val joinedChannelsList : LiveData<List<JoinedChannelModel>> get() = _joinedChannelsList

    private var _joinedUser = MutableLiveData<JoinChannelUser?>()
    val joinedUser : LiveData<JoinChannelUser?> get() = _joinedUser

    private var _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    fun getChannelsList(organizationId : String) {
        val service = RetrofitClientInstance.retrofitInstance!!.create(ChannelsList::class.java)
        val call = service.getChannelList(organizationId)

        call!!.enqueue(object : Callback<List<ChannelModel>> {
            override fun onResponse(call: Call<List<ChannelModel>>, response: Response<List<ChannelModel>>) {
                if (response.isSuccessful){
                    val res : List<ChannelModel>? = response.body()
                    res?.let {
                        _channelsList.value = it
                    }
                }else{
                    _error.value = response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<List<ChannelModel>>, t: Throwable) {
                _error.value = "Unknown Error"
                t.printStackTrace()
            }
        })
    }

    fun getJoinedChannelsList(organizationId : String, userId : String) {
        val service = RetrofitClientInstance.retrofitInstance!!.create(ChannelsList::class.java)
        val call = service.getJoinedChannelList(organizationId, userId)

        call!!.enqueue(object : Callback<List<JoinedChannelModel>> {
            override fun onResponse(call: Call<List<JoinedChannelModel>>, response: Response<List<JoinedChannelModel>>) {
                val res : List<JoinedChannelModel>? = response.body()
                res?.let {
                    _joinedChannelsList.value = it
                }
            }

            override fun onFailure(call: Call<List<JoinedChannelModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    // This function gets called from the join channel button and retrieves organizationId, channelId and user
    fun joinChannel(organizationId : String, channelId : String,user : JoinChannelUser, ){
        viewModelScope.launch {
            try {
                val joinedUser = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.joinChannel(organizationId,channelId,user)
                joinedUser?.let {
                    _joinedUser.value = it
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}