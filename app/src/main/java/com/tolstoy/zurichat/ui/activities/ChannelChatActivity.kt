package com.tolstoy.zurichat.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.channel_info.ChannelInfoActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dev.ronnie.github.imagepicker.ImagePicker


class ChannelChatActivity : AppCompatActivity() {

    lateinit var imagePicker: ImagePicker
    private var mTopToolbar: Toolbar? = null

    //For Message Notifications
    private val CHANNEL_ID = "zuriChat_channels_id_01"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chat)

        mTopToolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(mTopToolbar)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        setUpApplicationTheme(this)
        val channelChatEdit = findViewById<EditText>(R.id.channel_chat_editText)
        val sendVoiceNote = findViewById<FloatingActionButton>(R.id.send_voice_btn)
        val sendMessage = findViewById<FloatingActionButton>(R.id.send_message_btn)
        var text: String? = null

        //This makes a call to the message notifications function
        createNotificationChannel()

        //This sends a notification, that a message has been sent, as soon as the send button is clicked
        sendMessage.setOnClickListener{
            sendNotification()
        }

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

    //Function to start message notification for Oreo version devices and above
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "ZuriChat Channels Message Notification"
            val descriptionText = "You've got a new message on Channels - ZuriChat"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //Function to start message notification for older version devices
    fun sendNotification() {
        val intent = Intent(this, ChannelChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.logo1)
        val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.splash_logo)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo1)
            .setContentTitle("ZuriChat Channels Message Notification")
            .setContentText("You've got a new message on Channels - ZuriChat")
            .setLargeIcon(bitmapLargeIcon)
//            .setStyle(NotificationCompat.BigTextStyle().bigText("You've got a new message on 4 Channels you belong to - ZuriChat"))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
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