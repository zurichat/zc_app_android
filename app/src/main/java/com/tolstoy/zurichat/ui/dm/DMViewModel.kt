package com.tolstoy.zurichat.ui.dm

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.data.repository.ChatsRepository
import com.tolstoy.zurichat.data.repository.FilesRepository
import com.tolstoy.zurichat.data.repository.RoomRepository
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.models.network_response.CreateRoom
import com.tolstoy.zurichat.models.network_response.CreateRoomResponse
import com.tolstoy.zurichat.models.network_response.FileUploadResponse
import com.tolstoy.zurichat.models.network_response.SendMessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
@HiltViewModel
class DMViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository,
    private val roomsRepository: RoomRepository,
    private val filesRepository: FilesRepository,
    application: Application): AndroidViewModel(application) {

    val attachmentUploadResponse: LiveData<FileUploadResponse> get() = _attachmentUploadResponse
    private val _attachmentUploadResponse = MutableLiveData<FileUploadResponse>()
    fun uploadAttachment(uri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _attachmentUploadResponse.postValue(filesRepository
                    .uploadFile(getApplication<Application>().applicationContext, uri))
            }catch(exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    val sendMessageResponse: LiveData<SendMessageResponse> get() = _sendMessageResponse
    private val _sendMessageResponse = MutableLiveData<SendMessageResponse>()
    fun sendMessage(roomId: String, message: Message){
        viewModelScope.launch {
            try {
                _sendMessageResponse.value = chatsRepository.sendMessage(roomId, message)
            }catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    suspend fun getMessages(roomId: String) = viewModelScope.async {
        return@async chatsRepository.getMessages(roomId)
    }.await()

    suspend fun createRoom(userId: String, otherUserId: String,
                           orgId: String = "614679ee1a5607b13c00bcb7") = viewModelScope.async {
        return@async roomsRepository.createRoom(CreateRoom(orgId, listOf(userId, otherUserId)))
    }.await()
}