package com.tolstoy.zurichat.ui.login

import androidx.lifecycle.*
import com.tolstoy.zurichat.data.repository.UserRepository
import com.tolstoy.zurichat.models.*
import com.tolstoy.zurichat.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {


    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    private val _logoutResponse = MutableLiveData<Result<LogoutResponse>>()
    val logoutResponse: LiveData<Result<LogoutResponse>> = _logoutResponse

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

    fun logout() {
        _logoutResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler) {
            val logoutResponse = repository.logout()
            if (logoutResponse.code() == 200 && logoutResponse.isSuccessful) {
                val body = logoutResponse.body()
                body?.let {
                    _logoutResponse.postValue(Result.Success(it))
                }
            }
        }


    }

    fun saveUserAuthState(value: Boolean) {
        repository.saveUserAuthState(value)
    }

    fun getUserAuthState(): Boolean {
        return repository.getUserAuthState()
    }

    fun clearUserAuthState() {
         repository.clearUserAuthState()
    }

    private fun getUser() = viewModelScope.launch(Dispatchers.IO) {
        repository.getUser().flowOn(Dispatchers.IO)
            .catch { Timber.e(it) }
            .collect { _user.postValue(it) }
    }

    fun saveUser(user: User) = viewModelScope.launch {
        repository.saveUser(user)
    }

    fun passwordReset(passwordReset: PasswordReset) {
        try {
            viewModelScope.launch {
                _passwordReset.postValue(Result.Success(repository.passwordReset(passwordReset)))
            }
        } catch (e: Exception) {
            _passwordReset.postValue(Result.Error(e))
        }


    }
}