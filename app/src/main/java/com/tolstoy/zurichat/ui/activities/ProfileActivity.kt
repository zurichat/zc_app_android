package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.profile.data.ProfilePayload
import com.tolstoy.zurichat.ui.profile.data.ProfileResponse
import com.tolstoy.zurichat.ui.profile.network.Constants
import com.tolstoy.zurichat.ui.profile.network.ProfileService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

class ProfileActivity: AppCompatActivity() {

    //prepare retrofit service
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: ProfileService = retrofit
        .create(ProfileService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val about = findViewById<ImageView>(R.id.edit_about)
        val camera = findViewById<ImageView>(R.id.img_camera)

        about.setOnClickListener {
            startActivity(Intent(this, ProfileAboutActivity::class.java))
        }

        camera.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view=layoutInflater.inflate(R.layout.dialog_layout,null)
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()

        }
    }



}