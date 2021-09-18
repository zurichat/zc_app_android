package com.tolstoy.zurichat.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    private val _saveUserResponse = MutableLiveData<Unit>()
    val saveUserResponse: LiveData<Unit> = _saveUserResponse

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
            _loginResponse.postValue(Result.Error(throwable))
        }

    fun login(loginBody: LoginBody) {
        _loginResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler) {
            val loginResponse = repository.login(loginBody)
            _loginResponse.postValue(Result.Success(loginResponse))
        }
    }

    fun setLoggedIn(value: Boolean) = repository.setLoggedIn(value)

    fun isLoggedIn() = repository.getLoggedIn()

    fun saveUser(userEntity: UserEntity) = viewModelScope.launch {
        repository.saveUser(userEntity)
            .flowOn(dispatcher)
            .collect { _saveUserResponse.postValue(it) }
    }
}
