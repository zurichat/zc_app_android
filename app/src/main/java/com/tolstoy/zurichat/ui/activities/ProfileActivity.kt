package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tolstoy.zurichat.R

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val about = findViewById<ImageView>(R.id.edit_about)

        about.setOnClickListener {
            startActivity(Intent(this, ProfileAboutActivity::class.java))
        }
    }
}