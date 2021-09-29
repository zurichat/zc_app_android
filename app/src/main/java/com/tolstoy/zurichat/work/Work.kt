package com.tolstoy.zurichat.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class Work (context: Context, params: WorkerParameters): Worker(context, params) {

    private val dao = WorkDb.getDatabase(context).dao()
    private val composite = CompositeDisposable()

    override fun doWork(): Result {
        return try {
            composite.add(dao.addUser(UserData.getWorkUsers()).subscribeOn(Schedulers.io()).subscribe())
            Result.success()
        }
        catch (e: Exception){
            Result.failure()
        }
    }

    override fun onStopped() {
        super.onStopped()
        composite.dispose()
    }
}