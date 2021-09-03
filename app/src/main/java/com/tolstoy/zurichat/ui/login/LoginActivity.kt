package com.tolstoy.zurichat.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.HomePageActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)


        val btnVerifyEmail = findViewById<Button>(R.id.btn_verify_email1)
        btnVerifyEmail.setOnClickListener{
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
    }
}