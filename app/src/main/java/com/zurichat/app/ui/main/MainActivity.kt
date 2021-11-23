package com.zurichat.app.ui.main

import android.os.Bundle
import com.zurichat.app.R
import com.zurichat.app.databinding.ActivityMainBinding
import com.zurichat.app.ui.base.BaseActivity
import com.zurichat.app.utils.views.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI()
    }

    private fun setupUI(): Unit = with(binding){
        // loads back the main theme after displaying the splash screen
        setTheme(R.style.Theme_ZuriChat_NoActionBar)

        setContentView(binding.root)
    }
}