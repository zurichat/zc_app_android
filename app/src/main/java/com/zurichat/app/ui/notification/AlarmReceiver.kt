package com.zurichat.app.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("channel_tones", intent.getStringExtra("channel_tones"))
        service.putExtra("message_tone", intent.getStringExtra("message_tone"))
        service.putExtra("vibrate", intent.getStringExtra("vibrate" ))
        service.putExtra("high_priority", intent.getStringExtra("high_priority" ))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))



        context.startService(service)
    }
}