package com.tolstoy.zurichat.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult

class DMActivity : AppCompatActivity() {
    lateinit var imagePicker: ImagePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmactivity)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)
        val dmEditText = findViewById<EditText>(R.id.edittext_message)
        val sendVoiceNote = findViewById<FloatingActionButton>(R.id.fab_voiceNote)
        val sendMessage = findViewById<FloatingActionButton>(R.id.fab_send_text)
        val takePicture = findViewById<ImageView>(R.id.imageView_photo)
        var text: String? = null

        //initialize imagePicker library
        imagePicker = ImagePicker(this)

        // This listener checks whether the edittext has content that is not spaces
        // it then proceeds to update the floating action button respectively
        dmEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = s.toString()
                val currentMessage = dmEditText.text.toString()
                if (currentMessage.isEmpty()) {
                    sendMessage.isEnabled = false
                    sendVoiceNote.isEnabled = true
                } else {
                    sendMessage.isEnabled = true
                    sendVoiceNote.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //imageview button to take photo using camera
        takePicture.setOnClickListener {
            takePictureCamera()
        }

    }

    /**function to take image using camera
    we are using a library to easen the work
     */
    private fun takePictureCamera() {
        //take image
        imagePicker.takeFromCamera { imageResult ->
            //initialize the view where our image will be displayed
            val imageView = findViewById<ImageView>(R.id.dm_send_image)
            /**
             * when we take an image successfully,we take the uri and load using glide
             * we also set the view to visible
             */

            when (imageResult) {
                is ImageResult.Success -> {
                    val uri = imageResult.value

                    imageView.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(uri)
                        .into(imageView)

                }

                /**
                 * incase it's unsuccessful we toast the message and hide the image view
                 */
                is ImageResult.Failure -> {
                    imageView.visibility = View.GONE
                    Toast.makeText(this, "Picture not taken", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}