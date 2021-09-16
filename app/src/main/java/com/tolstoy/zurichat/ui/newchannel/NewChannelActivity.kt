package com.tolstoy.zurichat.ui.newchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding

class NewChannelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}