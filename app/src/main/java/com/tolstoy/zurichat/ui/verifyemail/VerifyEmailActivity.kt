package com.tolstoy.zurichat.ui.verifyemail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.otp.OtpPage
import com.tolstoy.zurichat.util.setUpApplicationTheme

class VerifyEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        val btn_verify_email: Button = findViewById(R.id.btn_verify_email)
        btn_verify_email.setOnClickListener {
            val int = Intent(this, OtpPage::class.java)
            startActivity(int)
        }
    }
}