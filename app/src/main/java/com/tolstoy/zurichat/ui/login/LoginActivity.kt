package com.tolstoy.zurichat.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.HomePageActivity
import com.tolstoy.zurichat.ui.activities.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val button: Button = findViewById(R.id.btn_verify_email1)

        button.setOnClickListener {
            val int = Intent(this, MainActivity::class.java)
            startActivity(int)
        }
    }
}