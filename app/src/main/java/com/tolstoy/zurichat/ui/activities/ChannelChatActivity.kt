package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.channel_info.ChannelInfoActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult


class ChannelChatActivity : AppCompatActivity() {

    lateinit var imagePicker: ImagePicker
    private var mTopToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chat)

        mTopToolbar = findViewById(R.id.custom_toolbar)
        setSupportActionBar(mTopToolbar)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        setUpApplicationTheme(this)
        val channelChatEdit = findViewById<EditText>(R.id.channel_chat_editText)
        val sendVoiceNote = findViewById<FloatingActionButton>(R.id.send_voice_btn)
        val sendMessage = findViewById<FloatingActionButton>(R.id.send_message_btn)
        var text: String? = null

        //Change Icon on editText Click
        val currentMessage = channelChatEdit.text.toString()
        if (currentMessage.isEmpty()) {
            sendMessage.isEnabled = false
            sendVoiceNote.isEnabled = true
        } else {
            sendMessage.isEnabled = true
            sendVoiceNote.isEnabled = false
        }

        channelChatEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = s.toString()
                val currentMessage = channelChatEdit.text.toString()
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


        //initialize imagePicker library
        imagePicker = ImagePicker(this)

        //Launch Attachment Popup
        val attachment = findViewById<ImageView>(R.id.channel_link)
        val popupView: View = layoutInflater.inflate(R.layout.attachment_popup, null)
        var popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.channel_chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.channel_info ->{
                intent = Intent(this, ChannelInfoActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}