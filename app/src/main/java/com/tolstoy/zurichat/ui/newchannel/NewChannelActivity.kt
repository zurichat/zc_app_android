package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityNewChannelBinding
import com.tolstoy.zurichat.models.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChannelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewChannelBinding
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_channel)
        supportActionBar?.hide()

        if (intent.hasExtra("USER")) {
            user = intent.getParcelableExtra("USER")
        } else {
            user = intent.getParcelableExtra("user")
        }

    }
}
