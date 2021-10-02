package com.tolstoy.zurichat.ui.fragments.home_screen

import androidx.lifecycle.*
import com.tolstoy.zurichat.data.remoteSource.postValue
import com.tolstoy.zurichat.data.remoteSource.retrieve
import com.tolstoy.zurichat.data.repository.DMRepository
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.data.repository.OrganizationRepository
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.JoinedChannelModel
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.OrganizationMembers
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChatsAdapter
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.networking.ChannelsList
import com.tolstoy.zurichat.ui.fragments.networking.JoinNewChannel
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 4:41 AM
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class HomeScreenViewModel
@Inject constructor(
    private val dmRoomsRepository: DMRepository,
    userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository
): ViewModel() {
    val searchQuery = MutableLiveData<String>()

    private var _members = MutableLiveData<OrganizationMembers>()
    val members: LiveData<OrganizationMembers> get() = _members

    private var _channelsList = MutableLiveData<List<ChannelModel>>()
    val channelsList : LiveData<List<ChannelModel>> get() = _channelsList

    private var _joinedChannelsList = MutableLiveData<List<JoinedChannelModel>>()
    val joinedChannelsList : LiveData<List<JoinedChannelModel>> get() = _joinedChannelsList

    private var _joinedUser = MutableLiveData<JoinChannelUser?>()
    val joinedUser : LiveData<JoinChannelUser?> get() = _joinedUser

    private var _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    private var _userRooms = MutableLiveData<List<Room>>()
    val userRooms: LiveData<List<Room>> get() = _userRooms

    private var _userChats = MutableLiveData<List<ChatsAdapter.Chat>>()
    val userChats: LiveData<List<ChatsAdapter.Chat>> get() = _userChats

    val userId = userRepository.getUserId()
    val orgId = organizationRepository.getId()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
        _error.value = exception.message
    }

    init {
        viewModelScope.launch(handler) {
            launch(handler) {
                getRooms()
            }
            launch(handler) {
                getMembers()
            }
        }
    }

    suspend fun getRooms() = viewModelScope.launch(handler) {
        _userRooms.postValue(dmRoomsRepository.getRooms(orgId, userId), _error)
    }

    suspend fun getMembers() = viewModelScope.launch(handler) {
        _members.postValue(organizationRepository.getMembers(), _error)
    }

    private suspend fun getChat(room: Room): ChatsAdapter.Chat? {
        // get the details of the other user
        val otherId = room.roomUserIds.first { it != userId }
        organizationRepository.getMember(otherId, orgId).retrieve(_error)?.let{ member ->
            dmRoomsRepository.getMessages(organizationRepository.getId(), room.id).retrieve(_error)?.let{ messages ->
                return ChatsAdapter.Chat(member.displayName, member.imageUrl, messages.messages.last(), 5)
            }
        }
        return null
    }

    fun getChats(rooms: List<Room>) = viewModelScope.launch(handler) {
        val result = mutableListOf<ChatsAdapter.Chat>()
        rooms.forEach { result.add(getChat(it)!!) }
        _userChats.value = result
    }

    suspend fun getOrganizationMember(memberId: String) = organizationRepository.getMember(memberId)

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
        viewModelScope.launch(handler) {
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