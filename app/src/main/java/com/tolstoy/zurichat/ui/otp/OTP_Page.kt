package com.tolstoy.zurichat.ui.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityOtpPageBinding
import com.tolstoy.zurichat.ui.activities.HomePageActivity
import com.tolstoy.zurichat.ui.activities.MainActivity

class   OTP_Page : AppCompatActivity() {

    private val binding: ActivityOtpPageBinding by lazy {
        ActivityOtpPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val verifyCodeBtn: Button = findViewById(R.id.otpVerifyCodeBtn)

        verifyCodeBtn.setOnClickListener {
            val int = Intent(this, HomePageActivity::class.java)
            startActivity(int)
        }

    }
}