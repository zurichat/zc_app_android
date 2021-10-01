package com.tolstoy.zurichat.ui.dm.dm_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.tolstoy.zurichat.R

const val CHANNEL_ID = "001"
const val CHANNEL_NAME = "Dm notification"
const val DESCRIPTION = "Notify a user when he/she has an unread message"

fun makeNotification(context: Context, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = CHANNEL_NAME
        val descriptionText = DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_nav)
            .setDestination(R.id.dmFragment)
            .createPendingIntent()

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                       .setContentText(message)
                       .setAutoCancel(true)
                       .setContentTitle("New message")
                       .setPriority(NotificationCompat.PRIORITY_HIGH)
                       .setSmallIcon(R.drawable.notifications_icon)
                       .setContentIntent(pendingIntent)
                       .build()
   NotificationManagerCompat.from(context).notify(1, notification)
}
