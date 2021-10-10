package com.zurichat.app.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.models.JoinedChannelModel
import com.zurichat.app.ui.fragments.model.*
import com.zurichat.app.ui.fragments.networking.ChannelsList
import com.zurichat.app.ui.fragments.networking.JoinNewChannel
import com.zurichat.app.ui.fragments.networking.RetrofitClientInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private var _newMessage = MutableLiveData<Data>()
    val newMessage : LiveData<Data> get() = _newMessage

    private var _connected = MutableLiveData<Boolean>()
    val connected : LiveData<Boolean> get() = _connected

    private var _joinedRoomData = MutableLiveData<List<RoomData>>()
    val joinedRoomData : LiveData<List<RoomData>> get() = _joinedRoomData

    private var _roomData = MutableLiveData<RoomData>()
    val roomData : LiveData<RoomData> get() = _roomData

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
    fun joinChannel(organizationId: String, channelId: String, user: JoinChannelUser){
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

    //Notifies List Of New Unread Messages
    fun notifyList(data : Data){
        viewModelScope.launch {
            _newMessage.value = data
        }
    }

    fun isConnected(isConnected : Boolean){
        _connected.value = isConnected
    }

    fun addToRoomData(roomData: RoomData){
        if (_joinedRoomData.value == null){
            _joinedRoomData.value = ArrayList()
        }

        val mutableDataList = _joinedRoomData.value!!.toMutableList()
        if (!mutableDataList.contains(roomData)){
            mutableDataList.add(roomData)
        }
        _joinedRoomData.value =  mutableDataList
    }

    // This function gets called after entering a channel to get the Centrifugo socket is
    fun retrieveRoomData(organizationId : String, channelId : String){
        viewModelScope.launch {
            try {
                val room = RetrofitClientInstance.retrofitInstance?.create(JoinNewChannel::class.java)?.getRoom(organizationId,channelId)
                room?.let {
                    _roomData.value = it
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}