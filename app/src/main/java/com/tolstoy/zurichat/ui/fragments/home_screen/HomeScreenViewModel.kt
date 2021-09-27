package com.tolstoy.zurichat.ui.fragments.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.data.repository.RoomRepository
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:41 AM
 */
@HiltViewModel
class HomeScreenViewModel
@Inject constructor(private val roomsRepository: RoomRepository): ViewModel() {
    val searchQuery = MutableLiveData<String>()

    val user by lazy { Cache.map["user"] as? User }

    val userRooms: LiveData<List<Room>>
        get() = _userRooms
    private val _userRooms = MutableLiveData<List<Room>>()
    fun getRooms(){
        viewModelScope.launch {
            try {
                _userRooms.value = roomsRepository.getRooms(userId = user!!.id)
            }catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }
}