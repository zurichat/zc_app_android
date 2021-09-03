package com.tolstoy.zurichat.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.HomePageActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.verifyemail.VerifyEmailActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        val button: Button = findViewById(R.id.btn_verify_email1)
        button.setOnClickListener {
            val int = Intent(this, VerifyEmailActivity::class.java)
            startActivity(int)
        }
    }
}