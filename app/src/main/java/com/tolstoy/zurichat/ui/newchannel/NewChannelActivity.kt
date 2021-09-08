package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityChannelNewBinding

class NewChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChannelNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}