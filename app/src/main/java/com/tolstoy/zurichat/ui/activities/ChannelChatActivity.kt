package com.tolstoy.zurichat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.setUpApplicationTheme

class ChannelChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chat)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)
    }
}