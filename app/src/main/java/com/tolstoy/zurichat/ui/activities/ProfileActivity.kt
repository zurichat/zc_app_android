package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tolstoy.zurichat.R
import timber.log.Timber

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val about = findViewById<ImageView>(R.id.edit_about)
        val camera = findViewById<ImageView>(R.id.img_camera)
        val phoneEdit = findViewById<ImageView>(R.id.imgPhone_editBT)

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

        // The following lines of code creates a dialog to change the Phone Number of the
        // user.
        phoneEdit.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.phoneno_edittext, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.et_phoneno)

            with(builder){
                setTitle("Edit Phone Number")
                setPositiveButton("Save"){ _, _ ->
                    editText.text.toString()
                }
                setNegativeButton("Cancel") { _, _ ->
                    Timber.d("This button clicked successfully!!") //just for log purposes
                }
                setView(dialogLayout)
                show()
            }

        }
    }


}

