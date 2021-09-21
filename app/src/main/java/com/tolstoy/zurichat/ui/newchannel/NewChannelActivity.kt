package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding
import com.tolstoy.zurichat.util.setUpApplicationTheme

class NewChannelActivity : AppCompatActivity() {

    private val binding: ActivityNewChannelBinding by lazy {
        ActivityNewChannelBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
