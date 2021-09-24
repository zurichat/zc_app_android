package com.tolstoy.zurichat.ui.fragments.home_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeScreenViewModel: ViewModel() {
    val searchQuery = MutableLiveData<String>()
}