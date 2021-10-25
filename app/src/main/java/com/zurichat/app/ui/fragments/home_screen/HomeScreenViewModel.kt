package com.zurichat.app.ui.fragments.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.data.remoteSource.retrieve
import com.zurichat.app.data.repository.OrganizationRepository
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.Room
import com.zurichat.app.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:41 AM
 */
@HiltViewModel
class HomeScreenViewModel
@Inject constructor(
    private val usersRepository: UserRepository,
    private val orgRepo: OrganizationRepository
    ): ViewModel() {
    val searchQuery = MutableLiveData<String>()

    val user by lazy { Cache.map["user"] as? User }

    val userRooms: LiveData<List<Room>>
        get() = _userRooms
    private val _userRooms = MutableLiveData<List<Room>>()


    fun getRooms() = viewModelScope.launch {
        val result = usersRepository.getRooms(userId = "61467ee61a5607b13c00bcf2", orgId = orgRepo.getId()).retrieve(null)?.let{
            _userRooms.value = it
        } //userId = user!!.id
    }
}