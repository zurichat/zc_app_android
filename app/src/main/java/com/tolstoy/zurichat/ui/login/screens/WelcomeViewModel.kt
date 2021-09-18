package com.tolstoy.zurichat.ui.login.screens

import androidx.lifecycle.ViewModel
import com.tolstoy.zurichat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor( private  val repository: UserRepository): ViewModel(){

    var isLoggedIn = repository.isLoggedIn

}