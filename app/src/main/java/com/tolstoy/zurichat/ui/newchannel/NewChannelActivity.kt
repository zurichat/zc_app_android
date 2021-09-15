package com.tolstoy.zurichat.ui.newchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding

class NewChannelActivity : AppCompatActivity() {

    private val binding: ActivityNewChannelBinding by lazy {
        ActivityNewChannelBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}