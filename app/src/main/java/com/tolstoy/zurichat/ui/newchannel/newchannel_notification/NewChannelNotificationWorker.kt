package com.tolstoy.zurichat.ui.newchannel.newchannel_notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NewChannelNotificationWorker(context: Context, params: WorkerParameters)
    : Worker(context, params) {


    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}