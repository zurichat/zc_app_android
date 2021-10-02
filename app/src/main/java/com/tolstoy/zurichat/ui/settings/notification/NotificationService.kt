package com.tolstoy.zurichat.ui.settings.notification

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.activities.MainActivity

class NotificationService: Service() {

    private val channelId = "Notification from Service"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }
    }
      override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val input = intent?.getStringExtra("inputExtra")
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0
            )
            val notification: Notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.notifications_icon)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(1, notification)
            return START_NOT_STICKY
        }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}

    fun onBind(intent: Intent?): IBinder? {
        return null
    }