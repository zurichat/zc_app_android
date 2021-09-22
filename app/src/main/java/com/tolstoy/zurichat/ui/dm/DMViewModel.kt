package com.tolstoy.zurichat.ui.dm

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.tolstoy.zurichat.data.repository.DmRepository
import com.tolstoy.zurichat.data.repository.ImagesRepository
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.network_response.ImageUploadResponse
import com.tolstoy.zurichat.models.network_response.MessageResponse
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
class DMViewModel(
    private val dmRepository: DmRepository,
    private val imagesRepository: ImagesRepository): ViewModel() {

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            val user = Cache.map["user"] as User
//            _userRooms.value = dmRepository.getRooms(user.id)
//        }
    }

    val imageUploadResonse: LiveData<ImageUploadResponse>
        get() = _imageUploadResonse
    private val _imageUploadResonse = MutableLiveData<ImageUploadResponse>()

    fun uploadImage(context: Context, uri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _imageUploadResonse.postValue(imagesRepository.uploadImage(context, uri))
            }catch(exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    val sendMessageResponse: LiveData<MessageResponse>
        get() = _sendMessageResponse
    private val _sendMessageResponse = MutableLiveData<MessageResponse>()
    fun sendMessage(roomId: String, message: Message){
        viewModelScope.launch {
            try {
                _sendMessageResponse.value = dmRepository.sendMessage(roomId, message)
            }catch (exception: Exception){
                exception.printStackTrace()
            }
        }
    }

    val userRooms: LiveData<List<RoomInfoResponse>>
        get() = _userRooms
    private val _userRooms = MutableLiveData<List<RoomInfoResponse>>()
}