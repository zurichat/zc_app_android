package com.zurichat.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.databinding.ktx.BuildConfig
import com.zurichat.app.data.localSource.StarredMessagesDatabase
import com.zurichat.app.data.repository.StarredMessagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class ZuriChatApplication: Application() {

    // Needed for StarredMessages to work
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { StarredMessagesDatabase.getDatabase(this) }
    val repository by lazy { StarredMessagesRepository(database.StarredMessagesDao()) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
