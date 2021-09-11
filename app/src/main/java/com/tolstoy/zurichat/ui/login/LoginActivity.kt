package com.tolstoy.zurichat.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)
    }
}