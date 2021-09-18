package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding

class NewChannelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}