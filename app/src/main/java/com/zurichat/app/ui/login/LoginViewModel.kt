package com.zurichat.app.ui.login

import android.widget.Toast
import androidx.lifecycle.*
import com.zurichat.app.data.remoteSource.postValue
import com.zurichat.app.data.repository.UserRepository
import com.zurichat.app.models.*
import com.zurichat.app.ui.login.password.confirm.ConfirmPasswordData
import com.zurichat.app.ui.login.password.confirm.ConfirmResponse
import com.zurichat.app.ui.login.password.resetuserpass.ResetUserPasswordData
import com.zurichat.app.ui.login.password.resetuserpass.ResetUserPasswordResponse
import com.zurichat.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {


    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    private val _logoutResponse = MutableLiveData<Result<LogoutResponse>>()
    val logoutResponse: LiveData<Result<LogoutResponse>> = _logoutResponse

    private val _confirmPassResponse = MutableLiveData<Result<ConfirmPassResponse>>()
    val confirmPassResponse: LiveData<Result<ConfirmPassResponse>> = _confirmPassResponse

    private val _resetCodeResponse = MutableLiveData<Result<ResetCodeResponse>>()
    val resetCodeResponse: LiveData<Result<ResetCodeResponse>> = _resetCodeResponse

    private val _updatePassResponse = MutableLiveData<Result<LogoutResponse>>()
    val updatePassResponse: LiveData<Result<LogoutResponse>> = _updatePassResponse

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _passwordReset = MutableLiveData<Result<PassswordRestReponse>>()
    val pssswordreset: LiveData<Result<PassswordRestReponse>> get() = _passwordReset

    private val _confirmPassword = MutableLiveData<Result<ConfirmResponse>>()
    val confirmPassword: LiveData<Result<ConfirmResponse>> get() = _confirmPassword

    private val _resetUserPassword = MutableLiveData<Result<ResetUserPasswordResponse>>()
    val resetUserPassword: LiveData<Result<ResetUserPasswordResponse>> get() = _resetUserPassword

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

    fun logout(logoutBody: LogoutBody) {
        _logoutResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler) {
            val logoutResponse = repository.logout(logoutBody)
            if (logoutResponse.code() == 200 && logoutResponse.isSuccessful) {
                val body = logoutResponse.body()
                body?.let {
                    _logoutResponse.postValue(Result.Success(it))
                }
            }else {
                _logoutResponse.postValue(Result.Error(Exception("ERROR MESSAGE")))
            }
        }
    }

    fun confirmPass( confirmPassBody: ConfirmPassBody){
        _confirmPassResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler){
            val confirmPassResponse = repository.confirmPass(confirmPassBody)
            _confirmPassResponse.postValue(Result.Success(confirmPassResponse))
        }

    }

    fun verifyResetCode(resetCodeBody: ResetCodeBody){
        _resetCodeResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler){
            val resetCodeResponse = repository.verifyResetCode(resetCodeBody)
            if (resetCodeResponse.code() == 200 && resetCodeResponse.isSuccessful) {
                val body = resetCodeResponse.body()
                body?.let {
                    _resetCodeResponse.postValue(Result.Success(it))
                }

            } else {
                _resetCodeResponse.postValue(Result.Error(Exception("ERROR MESSAGE")))
            }

        }

    }

    fun updatePassword(updatePassBody: UpdatePassBody,verificationCode:String){
        _updatePassResponse.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler){
            val updatePassResponse = repository.updatePassword(updatePassBody, verificationCode)
            if (updatePassResponse.code() == 200 && updatePassResponse.isSuccessful) {
                val body = updatePassResponse.body()
                body?.let {
                    _updatePassResponse.postValue(Result.Success(it))
                }

            } else {
                _updatePassResponse.postValue(Result.Error(Exception("ERROR MESSAGE")))
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
        _user.postValue(repository.getUser())
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

    fun confirmPassword(confirmPasswordData: ConfirmPasswordData){
        try {
            viewModelScope.launch {
                _confirmPassword.postValue(Result.Success(repository.confirmPassword( confirmPasswordData)))
            }
        }catch (e:Exception) {
           _confirmPassword.postValue(Result.Error(e))
        }
    }

    fun resetUserPassword(resetUserPasswordData: ResetUserPasswordData){
        try {
            viewModelScope.launch {
                _resetUserPassword.postValue(Result.Success(repository.resetUserPassword(resetUserPasswordData)))
            }
        }catch (e:Exception){
            _resetUserPassword.postValue(Result.Error(e))
        }
    }
}