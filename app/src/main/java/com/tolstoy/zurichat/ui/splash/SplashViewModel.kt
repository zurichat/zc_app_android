package com.tolstoy.zurichat.ui.splash

import androidx.lifecycle.ViewModel
import com.tolstoy.zurichat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun isLoggedIn() = userRepository.getLoggedIn()
}
