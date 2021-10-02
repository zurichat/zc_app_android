package com.tolstoy.zurichat.ui.fragments.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.data.remoteSource.retrieve
import com.tolstoy.zurichat.data.repository.DMRepository
import com.tolstoy.zurichat.data.repository.OrganizationRepository
import com.tolstoy.zurichat.data.repository.orgId
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
@Inject constructor(
    private val dmRepo: DMRepository,
    private val orgRepo: OrganizationRepository
    ): ViewModel() {
    val searchQuery = MutableLiveData<String>()

    val user by lazy { Cache.map["user"] as? User }

    val userRooms: LiveData<List<Room>>
        get() = _userRooms
    private val _userRooms = MutableLiveData<List<Room>>()


    fun getRooms() = viewModelScope.launch {
        val result = dmRepo.getRooms(userId = "61467ee61a5607b13c00bcf2", orgId = orgRepo.getId()).retrieve(null)?.let{
            _userRooms.value = it
        } //userId = user!!.id
    }
}