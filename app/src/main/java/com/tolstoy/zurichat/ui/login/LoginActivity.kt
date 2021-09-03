package com.tolstoy.zurichat.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.DMActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        startActivity(Intent(this, DMActivity::class.java))
    }
}