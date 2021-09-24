package com.tolstoy.zurichat.ui.login

import androidx.lifecycle.*
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.*
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.mapToApp
import com.tolstoy.zurichat.util.mapToEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _passwordReset = MutableLiveData<Result<PassswordRestReponse>>()
              val pssswordreset: LiveData<Result<PassswordRestReponse>> get() = _passwordReset

    init {
        getUser()
    }

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

    fun saveUserAuthState(value: Boolean) {
        repository.saveUserAuthState(value)
    }

    fun getUserAuthState(): Boolean {
        return repository.getUserAuthState()
    }

    private fun getUser() = viewModelScope.launch {
        repository.getUser().flowOn(Dispatchers.IO)
            .map { it.mapToApp() }
            .catch { Timber.e(it) }
            .collect { _user.postValue(it) }
    }

    fun saveUser(user: User) = viewModelScope.launch {
        repository.saveUser(user.mapToEntity())
    }

    fun passwordReset(passwordReset: PasswordReset) {
        try {
            viewModelScope.launch {
                _passwordReset.postValue(Result.Success(repository.passwordReset(passwordReset)))
            }
        }catch (e:Exception){
            _passwordReset.postValue(Result.Error(e))
        }


    }
}