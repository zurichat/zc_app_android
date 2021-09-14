package com.tolstoy.zurichat.ui.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.networking.ChannelsList
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Viewmodel to handle updates to the list when a network call is made and result is retrieved
 */
class ChannelViewModel : ViewModel() {

    private var _channelsList = MutableLiveData<List<ChannelModel>>()
    val channelsList : LiveData<List<ChannelModel>>
        get() = _channelsList

    fun getChannelsList() {
        val service = RetrofitClientInstance.retrofitInstance!!.create(ChannelsList::class.java)
        val call = service.channelList

        call!!.enqueue(object : Callback<List<ChannelModel>> {
            override fun onResponse(call: Call<List<ChannelModel>>, response: Response<List<ChannelModel>>) {
                val res : List<ChannelModel>? = response.body()
                res?.let {
                    _channelsList.value = it
                }
            }

            override fun onFailure(call: Call<List<ChannelModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}