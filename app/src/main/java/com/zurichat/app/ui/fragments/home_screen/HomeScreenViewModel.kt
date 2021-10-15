package com.zurichat.app.ui.fragments.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.remoteSource.postValue
import com.zurichat.app.data.repository.DMRepository
import com.zurichat.app.data.repository.OrganizationRepository
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.Room
import com.zurichat.app.models.User
import com.zurichat.app.models.network_response.OrganizationMembers
import com.zurichat.app.ui.fragments.home_screen.adapters.ChatsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:41 AM
 */
@HiltViewModel
class HomeScreenViewModel
@Inject constructor(
    private val dmRepo: DMRepository,
    private val orgRepo: OrganizationRepository,
    private val userRepo: UserRepository
): ViewModel() {

    val searchQuery = MutableLiveData<String>()

    private var _members = MutableLiveData<OrganizationMembers>()
    val members: LiveData<OrganizationMembers> get() = _members

    private var _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    private var _userRooms = MutableLiveData<List<Room>>()
    val userRooms: LiveData<List<Room>> get() = _userRooms

    val userId = userRepo.getUserId()
    val memberId = orgRepo.getMemberId()
    val orgId = orgRepo.getId()
    val testingMessages = listOf("Hey there", "Hi", "What's good?",
        "Are you available right now?", "Have you seen the news?",
        "Get back to me when you are available")
    lateinit var user: User

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.tag(TAG).e("Caught $exception")
        _error.value = exception.message
    }

    init {
        viewModelScope.launch(handler) {
            user = userRepo.getUser()
        }
    }

    fun getRooms() = viewModelScope.launch(handler) {
        _userRooms.postValue(dmRepo.getRooms(orgId, memberId), _error)
    }

    fun getMembers() = viewModelScope.launch(handler) {
        _members.postValue(orgRepo.getMembers(), _error)
    }

    suspend fun getChat(room: Room, members: List<OrganizationMember>) =
        withContext(viewModelScope.coroutineContext + handler) {
            // get the id of the other user
            val otherId = with(room.roomUserIds) { if (first() != memberId) first() else last() }
            val member = members.firstOrNull { it.id == otherId }
            ChatsAdapter.Chat(
                member?.name() ?: "Guest", member?.imageUrl ?: "", room,
                testingMessages[(Math.random() * testingMessages.size).toInt()],
                LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")),
                (Math.random() * 10).toInt()
            )
            // TODO: Get the last message from the room and display it with the chat
            //            dmRepo.getMessages(orgId, room.id).retrieve(_error)?.let{ messages ->
            //                chat.message = messages.messages.last()
            //            }
        }

    companion object {
        val TAG = HomeScreenViewModel::class.simpleName!!
    }
}