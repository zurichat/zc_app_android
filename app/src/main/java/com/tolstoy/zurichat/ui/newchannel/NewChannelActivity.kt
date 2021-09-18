package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding

class NewChannelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_channel)
    }
}
