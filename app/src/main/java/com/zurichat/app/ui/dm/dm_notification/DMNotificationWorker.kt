package com.zurichat.app.ui.dm.dm_notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DMNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}