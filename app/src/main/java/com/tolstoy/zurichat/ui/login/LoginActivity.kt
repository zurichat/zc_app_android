package com.tolstoy.zurichat.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.HomePageActivity

import com.tolstoy.zurichat.ui.activities.MainActivity

import com.tolstoy.zurichat.util.setUpApplicationTheme


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button: Button = findViewById(R.id.btn_verify_email1)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)


        button.setOnClickListener {
            val int = Intent(this, MainActivity::class.java)
            startActivity(int)
        }
    }
}