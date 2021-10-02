package com.tolstoy.zurichat.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityNoticicationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private val binding: ActivityNoticicationBinding by lazy{
        ActivityNoticicationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        if (intent.getBooleanExtra("notification", false)) { //Just for confirmation
            binding.txtTitleView.text  = intent.getStringExtra("title")
            binding.txtMsgView.text = intent.getStringExtra("message")

        }
    }
}