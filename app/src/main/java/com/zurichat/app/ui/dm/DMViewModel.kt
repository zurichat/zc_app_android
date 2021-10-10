package com.zurichat.app.ui.dm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.zurichat.app.data.remoteSource.postValue
import com.zurichat.app.data.remoteSource.retrieve
import com.zurichat.app.data.repository.DMRepository
import com.zurichat.app.data.repository.FilesRepository
import com.zurichat.app.data.repository.OrganizationRepository
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.Message
import com.zurichat.app.models.network_response.CreateRoom
import com.zurichat.app.models.network_response.FileUploadResponse
import com.zurichat.app.models.network_response.GetMessagesResponse
import com.zurichat.app.models.network_response.SendMessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class DMViewModel
@Inject constructor(
    private val dmRepository: DMRepository,
    private val orgRepo: OrganizationRepository,
    private val filesRepository: FilesRepository,
    private val userRepository: UserRepository,
    application: Application): AndroidViewModel(application) {

    val userId by lazy { userRepository.getUserId() }

    private var _error = MutableLiveData<String?>()
    val error : LiveData<String?> get() = _error

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e("Caught $exception")
        _error.value = exception.message
    }

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
    fun sendMessage(roomId: String, message: Message) = viewModelScope.launch(handler){
        _sendMessageResponse.postValue(dmRepository.sendMessage(orgRepo.getId(), roomId, message), _error)
    }

    val messagesResponse: LiveData<GetMessagesResponse> get() = _messagesResponse
    private val _messagesResponse = MutableLiveData<GetMessagesResponse>()
    suspend fun getMessages(roomId: String) = viewModelScope.launch {
        _messagesResponse.postValue(dmRepository.getMessages(orgRepo.getId(), roomId), _error)
    }

    suspend fun createRoom(userId: String, otherUserId: String) = viewModelScope.async {
        val orgId = orgRepo.getId()
        return@async dmRepository.createRoom(orgId, userId, CreateRoom(orgId, listOf(userId, otherUserId))).retrieve(_error)
    }.await()
}


