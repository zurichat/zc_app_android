package com.tolstoy.zurichat.ui.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.ui.adapters.DmMessagesRecyclerAdapter
import com.tolstoy.zurichat.ui.dm_channels.adapters.MessageAdapter
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import java.text.SimpleDateFormat
import java.util.*

class DMActivity : AppCompatActivity() {
    lateinit var imagePicker: ImagePicker
    private val adapter by lazy { MessageAdapter(this, 0) }
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

        // creating a demo conversation for a DM message
        val demoDmMessages = mutableListOf(
            DmMessages("me", "Hello! I'm Godfirst", "6:00 AM"),
            DmMessages("me", "How are you doing?", "6:01 AM"),
            DmMessages("", "Fine. I'm ChibuFirst", "6:05 AM"),
            DmMessages("", "Nice to meet you, Godfirst", "6:06 AM"),
            DmMessages("me", "Same here... Which team?", "6:08 AM"),
            DmMessages("me", "Which stage are you?", "6:08 AM")
        )
        //update chat screen
        update(demoDmMessages)

        //call the sendMessage function when button is clicked and pass message as argument
        sendMessage.setOnClickListener {
            val message = dmEditText.text.toString()
            //function to format time
            fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
                val formatter = SimpleDateFormat(format, locale)
                return formatter.format(this)
            }
            //function to get system current time
            fun getCurrentDateTime(): Date {
                return Calendar.getInstance().time
            }
            val currentTime = getCurrentDateTime()
            val time = currentTime.toString("HH:mm")
            val dms = DmMessages("me", "$message", time)
            demoDmMessages.add(dms)
            update(demoDmMessages)
            dmEditText.text.clear()
        }

        //Launch Attachment popup
        val attachment = findViewById<ImageView>(R.id.imageView_attachment)
        val popupView: View = layoutInflater.inflate(R.layout.attachment_popup, null)
        var popupWindow = PopupWindow(
            popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
        }

    }

    fun update(demoDmMessages:MutableList<DmMessages>){
        // initializing the DM message adapter
        val dmMessagesAdapter = DmMessagesRecyclerAdapter(demoDmMessages, "me")
        // setting up the DM message RecyclerView
        val dmChatRecyclerView: RecyclerView = findViewById(R.id.dm_chat_recycler_view)
        dmChatRecyclerView.apply {
            val linearLayout = LinearLayoutManager(this@DMActivity)
//            linearLayout.stackFromEnd = true
            layoutManager = linearLayout
//            setHasFixedSize(true)
            adapter = this@DMActivity.adapter
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