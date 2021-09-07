package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.databinding.ActivityChannelNewBinding

class NewChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChannelNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}