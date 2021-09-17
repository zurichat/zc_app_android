package com.tolstoy.zurichat.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.profile.data.ProfilePayload
import com.tolstoy.zurichat.ui.profile.data.ProfileResponse
import com.tolstoy.zurichat.ui.profile.network.Constants
import com.tolstoy.zurichat.ui.profile.network.ProfileService
import dev.ronnie.github.imagepicker.ImagePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import timber.log.Timber

class ProfileActivity: AppCompatActivity() {

    //token id
    private var token: String? = null

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

            val gallery = view.findViewById<ImageView>(R.id.imageView_gallery)

            //initialize imagePicker library
            gallery.setOnClickListener {
                launcher.launch(
                    com.github.drjacky.imagepicker.ImagePicker.with(this)
                        //...
                        .galleryOnly()
                        .crop()
                        .createIntent()
                )

            }

        }
    }

    //start activity for result launcher for Image gallery
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image

            val profilePhoto = findViewById<ImageView>(R.id.profile_photo)

            profilePhoto.setImageURI(uri)
        }
    }


    //update profile details
    private fun updateProfile() {

        //demo data
        val profileData = ProfilePayload("Zuri chat member",
            "PorayMan",
            "09876543212")

        val authToken: String? = token

        val call: Call<ProfileResponse> = retrofitService.updateProfile(authToken, Constants.ORG_ID, Constants.MEM_ID, profileData)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>?
            ) {

                if(response!!.isSuccessful) {
                    Log.i("Login Response Result", response.body()!!.message)

                } else {
                    when(response.code()){
                        400 -> {
                            Log.e("Error 400", "invalid authorization")
                        }
                        404 -> {
                            Log.e("Error 404", "Not Found")
                        }
                        401 -> {
                            Log.e("Error 401", "No authorization or session expired")
                        }
                        else -> {
                            Log.e("Error", "Generic Error")
                        }
                    }
                }

            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Timber.e(t.message.toString())
            }

        })
    }

}