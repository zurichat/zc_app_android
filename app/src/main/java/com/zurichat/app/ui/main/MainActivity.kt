package com.zurichat.app.ui.main

import android.os.Bundle
import com.zurichat.app.R
import com.zurichat.app.databinding.ActivityMainBinding
import com.zurichat.app.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setupUI()
    }

    private fun setupUI(): Unit = with(binding){
        // loads back the main theme after displaying the splash screen
        setTheme(R.style.Theme_ZuriChat_NoActionBar)

        setContentView(binding.root)
    }
}