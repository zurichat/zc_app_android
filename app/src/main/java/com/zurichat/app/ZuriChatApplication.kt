package com.zurichat.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.databinding.ktx.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class ZuriChatApplication: Application() {

    // Needed for StarredMessages to work
//    val applicationScope = CoroutineScope(SupervisorJob())
//    val database by lazy { AppDatabase.StarredMessagesDao() }
//    val repository by lazy { UserRepository(database.StarredMessagesDao()) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
