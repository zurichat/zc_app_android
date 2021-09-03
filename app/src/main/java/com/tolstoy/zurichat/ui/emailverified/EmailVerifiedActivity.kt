package com.tolstoy.zurichat.ui.emailverified

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.setUpApplicationTheme

class EmailVerifiedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verified)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)
    }
}