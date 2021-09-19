package com.tolstoy.zurichat.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.profile.data.ProfilePayload
import com.tolstoy.zurichat.ui.profile.data.ProfileResponse
import com.tolstoy.zurichat.ui.profile.network.Constants
import com.tolstoy.zurichat.ui.profile.network.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class ProfileActivity: AppCompatActivity() {

    private lateinit var savedName : TextView
    private lateinit var savedAbout : TextView
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

        savedName = findViewById(R.id.saved_name)
        savedAbout = findViewById(R.id.saved_about)

        val about = findViewById<ImageView>(R.id.edit_about)
        val camera = findViewById<ImageView>(R.id.img_camera)
        val phoneEdit = findViewById<ImageView>(R.id.imgPhone_editBT)
        val userName = findViewById<ImageView>(R.id.edit_name)

        val dialog = CreateDialog(layoutInflater,this)

        val editNameDialog = dialog.createEditNameDialog(savedName)
        val editAboutDialog = dialog.createEditAboutDialog(savedAbout)

        userName.setOnClickListener {
            editNameDialog.show()
        }

        about.setOnClickListener {
            editAboutDialog.show()
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

            val cam = view.findViewById<ImageView>(R.id.imageView_cam)
            //launch camera
            cam.setOnClickListener {
                launcher.launch(
                    (
                            com.github.drjacky.imagepicker.ImagePicker.with(this)
                                .cameraOnly()
                                .crop()
                                .cropSquare()
                                .createIntent()
                            )
                )
            }


        }
        // The following lines of code creates a dialog to change the Phone Number of the
        // user.
        phoneEdit.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.phoneno_edittext, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_phoneno)
            val phoneTextView = findViewById<TextView>(R.id.tv_phoneno)

            with(builder){
                setTitle("Edit Phone Number")
                setPositiveButton("Save"){ _, _ ->
                    phoneTextView.text = editText.text.toString() // populates the value of the
                                                                    // EditText on the TextView
                }
                setNegativeButton("Cancel") { _, _ ->
                    Timber.d("This button clicked successfully!!") //just for log purposes
                }
                setView(dialogLayout)
                show()
            }

        }
    }


    //start activity for result launcher for Image gallery
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            val profilePhoto = findViewById<ImageView>(R.id.profile_photo)

            // Saves image URI as string to Default Shared Preferences
            val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString("image", java.lang.String.valueOf(uri))
            editor.commit()

            //set profile photo to image uri
            profilePhoto.setImageURI(uri)
            profilePhoto.invalidate()
        }
    }

    override fun onResume() {
        super.onResume()

        val profilePhoto = findViewById<ImageView>(R.id.profile_photo)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val mImageUri = preferences.getString("image", null)
        if(mImageUri != null){
            profilePhoto.setImageURI(Uri.parse(mImageUri))
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



