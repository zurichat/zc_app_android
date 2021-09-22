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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toFile
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.profile.data.*
import com.tolstoy.zurichat.ui.profile.network.Constants
import com.tolstoy.zurichat.ui.profile.network.ProfileService
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File

open class ProfileActivity: AppCompatActivity() {

    private lateinit var savedName : TextView
    private lateinit var savedAbout : TextView
    private var user : User? = null

    //token id
    private var token: String? = null
    private lateinit var orgMemId: String
    private lateinit var memId: String

    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }).build()

    //prepare retrofit service
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: ProfileService = retrofit
        .create(ProfileService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //user = intent.extras?.getParcelable("USER")!!

        user = intent.getParcelableExtra<User>("USER") as User
        setContentView(R.layout.activity_profile)

        token = user?.token

        getUserOrganization()

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

                    val phone: String = editText.text.toString()
                    updatePhone(phone)
                    Timber.d("Update Successful") // EditText on the TextView

                    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val editor = preferences.edit()
                    editor.putString("phone", java.lang.String.valueOf(editText.text.toString()))
                    editor.apply()

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
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
            updateProfilePhoto(uri)
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

        val phoneTextView = findViewById<TextView>(R.id.tv_phoneno)
        val phoneText = preferences.getString("phone", null)
        phoneTextView.text = phoneText

        val nameTextView = findViewById<TextView>(R.id.saved_name)
        val aboutTextView = findViewById<TextView>(R.id.saved_about)

        val nameText = preferences.getString("name", null)
        nameTextView.text = nameText

        val aboutText = preferences.getString("about", null)
        aboutTextView.text = aboutText
    }


    //update profile details
    private fun updateProfilePhone(orgMemId: String, memId: String, update: PhoneUpdate) {

        val call: Call<ProfileResponse> = retrofitService.updateProfilePhone(orgMemId, memId, update)

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
    private fun updateProfileName(orgMemId: String, memId: String, update: NameUpdate) {

        val call: Call<ProfileResponse> = retrofitService.updateProfileName(orgMemId, memId, update)

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
    private fun updateProfileBio(orgMemId: String, memId: String, update: AboutUpdate) {

        val call: Call<ProfileResponse> = retrofitService.updateProfileBio(orgMemId, memId, update)

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

    //Get user organization
    private fun getUserOrganization() {
        val email: String? = user?.email

        val call: Call<UserOrganizationResponse> = retrofitService.getUserOrg(email)
        call.enqueue(object : Callback<UserOrganizationResponse> {
            override fun onResponse(
                call: Call<UserOrganizationResponse>,
                response: Response<UserOrganizationResponse>?
            ) {
                if(response!!.isSuccessful) {
                    Log.i("Login Response Result", response.body()!!.message)

                    val orgId: String = response.body()!!.data[1].id

                    getMemberId(orgId)
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

            override fun onFailure(call: Call<UserOrganizationResponse>, t: Throwable) {
                Timber.e(t.message.toString())
            }

        })
    }

    private fun getMemberId(orgId: String) {
        val call: Call<UserMemberResponse> = retrofitService.getUserMemberId(orgId)
        call.enqueue(object : Callback<UserMemberResponse> {
            override fun onResponse(
                call: Call<UserMemberResponse>,
                response: Response<UserMemberResponse>?
            ) {
                if(response!!.isSuccessful) {
                    Log.i("Login Response Result", response.body()!!.message)

                    memId = response.body()!!.data[1]._id
                    orgMemId = response.body()!!.data[1].org_id
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

            override fun onFailure(call: Call<UserMemberResponse>, t: Throwable) {
                Timber.e(t.message.toString())
            }

        })
    }

    private fun updatePhone(phone: String) {
        val phoneData = PhoneUpdate(phone)
        updateProfilePhone(orgMemId, memId, phoneData)
    }

    fun updateName(name: String) {
        val nameData = NameUpdate(name)
        updateProfileName(orgMemId, memId, nameData)
    }

    fun updateAbout(bio: String) {
        val bioData = AboutUpdate(bio)
        updateProfileBio(orgMemId, memId, bioData)
    }

    private fun updateProfilePhoto(uri: Uri) {

        // use the FileUtils to get the actual file by uri
        val file: File = uri.toFile()

        // create RequestBody instance from file
        val requestFile: RequestBody =
            file.asRequestBody((contentResolver.getType(uri))?.toMediaType())

        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("picture", file.name, requestFile)

        val call: Call<ProfilePhotoResponse> = retrofitService.updatePhoto(orgMemId, memId, body)

        call.enqueue(object : Callback<ProfilePhotoResponse> {
            override fun onResponse(
                call: Call<ProfilePhotoResponse>,
                response: Response<ProfilePhotoResponse>?
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

            override fun onFailure(call: Call<ProfilePhotoResponse>, t: Throwable) {
                Timber.e(t.message.toString())
            }

        })
    }

}



