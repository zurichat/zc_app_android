package com.tolstoy.zurichat.ui.fragments.home_screen

import androidx.lifecycle.*
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.data.repository.OrganizationRepository
import com.tolstoy.zurichat.data.repository.RoomRepository
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.models.network_response.OrganizationMembers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:41 AM
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class HomeScreenViewModel
@Inject constructor(
    private val roomsRepository: RoomRepository,
    userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository
): ViewModel() {
    val searchQuery = MutableLiveData<String>()
    val members: LiveData<Result<OrganizationMembers>> get() = _members
    private lateinit var _members: LiveData<Result<OrganizationMembers>>

    val userId = userRepository.getUserId()

    val userRooms: LiveData<List<Room>>
        get() = _userRooms
    private val _userRooms = MutableLiveData<List<Room>>()

    init {
        viewModelScope.launch {
            launch {
                _userRooms.value = roomsRepository.getRooms(userId)
            }
            launch {
                _members = organizationRepository.getMembers().asLiveData()
            }
        }
    }
}