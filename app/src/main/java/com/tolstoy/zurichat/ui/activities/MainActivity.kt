package com.tolstoy.zurichat.ui.activities


import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragment
import com.tolstoy.zurichat.ui.settings.SettingsActivity
import com.tolstoy.zurichat.ui.notification.NotificationService
import com.tolstoy.zurichat.ui.notification.NotificationUtils
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id_example"
    private val notificationId = 101
    private var notificationSettings: SettingsActivity.NotificationAndSounds? = null
    val pattern = longArrayOf(100,200,300)
    val message_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    var messager_name: String = "Abass"
    var notification_message: String = "Notification"

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false


    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
        }
//        notificationSettings = SettingsActivity.NotificationAndSounds()
//        createNotificationChannel()
//        notificationSetting(messager_name,notification_message,pattern,message_sound)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        //Request permission for accessing media and files from storage
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            //Shows Toast message if permission is granted or denied.
            if (isGranted) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
            }
        }

        //This code runs automatically,
        //This checks if the permission has been granted, if it has, pass, else, it request for the permission using the function above
        //Comment this if and else statements to prevent permission from showing on startup.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Pass
        } else {
            //Request permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    // creation of notification bar
    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Notification Title"
            val descriptionText = "Notificaton Description"
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel : NotificationChannel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // show new messages of notification bar
    fun showNotification(name: String?, message: String?, pattern: LongArray?,sound: Uri?)
    {
        val intent = Intent(this,HomeScreenFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(name)
            .setContentText(message)
            .setVibrate(pattern)
            .setSound(sound)
            .setSmallIcon(R.drawable.ic_smile)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
//            notify(notificationId,builder.build())
        }
    }

    // setting of notification from notification and sound Fragment
    fun notificationSetting(name: String, message: String, vibratePattern: LongArray,messageSound: Uri)
    {
        if(notificationSettings!!.isChannelToneChecked)
        {
            showNotification(name,message,null,messageSound)
        }else if(notificationSettings!!.isChannelToneChecked && notificationSettings!!.isMessageToneChecked)
        {
            showNotification(name,message,null,messageSound)
        }else if(notificationSettings!!.isChannelToneChecked && notificationSettings!!.isMessageToneChecked
            && notificationSettings!!.isVibrateChecked)
        {
            showNotification(name,message,vibratePattern,messageSound)
        }else if(notificationSettings!!.isVibrateChecked)
        {
            showNotification(name,message,vibratePattern,null)
        }else if(notificationSettings!!.isChannelToneChecked && notificationSettings!!.isVibrateChecked)
        {
            showNotification(name,message,vibratePattern,messageSound)
        }else if(notificationSettings!!.isVibrateChecked && notificationSettings!!.isMessageToneChecked)
        {
            showNotification(name,message,vibratePattern,messageSound)
        }else if(notificationSettings!!.isHighPriorityChecked)
        {
            showNotification(name,message,null,null)
        }else{
            showNotification(null,null,null,null)
        }
    }

    fun startService(view: View) {
        val notificationAndSounds: String = "NotificationandSounds"
        val serviceIntent = Intent(this, NotificationService::class.java)
        serviceIntent.putExtra("channel_tones, message_tone,vibrate,high_priority", notificationAndSounds)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
    fun stopService(view: View) {
        val serviceIntent = Intent(this, NotificationService::class.java)
        stopService(serviceIntent)
    }

}

