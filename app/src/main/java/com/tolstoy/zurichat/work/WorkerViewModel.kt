package com.tolstoy.zurichat.work

import android.app.Application
import android.util.AndroidException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.tolstoy.zurichat.data.localSource.AppDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class WorkerViewModel (application: Application): AndroidViewModel(application){

    val users: MutableLiveData<List<User>> = MutableLiveData()
    val exception: MutableLiveData<String> = MutableLiveData()

    private val composite = CompositeDisposable()
    private val dao = WorkDb.getDatabase(application).dao()

    private val manager = WorkManager.getInstance(application)

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()

    private val oneTimeWorker = OneTimeWorkRequest.Builder(Work::class.java)
        .setConstraints(constraints)
        .build()

    private val periodicWork = PeriodicWorkRequest.Builder(Work::class.java, 15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    fun getWorkUsers(){
        composite.add(
            dao.getWorkUser()
               // .observeOn(AndroidScheduer.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(
                    {users.value },
                    {exception.value = it.message},)
        )
    }

    fun clearDb(){
        composite.add(dao.deleteUsers().subscribeOn(Schedulers.io()).subscribe())
    }

    fun startWork(){
        manager.enqueue(listOf(oneTimeWorker, periodicWork))
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}