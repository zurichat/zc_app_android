package com.tolstoy.zurichat

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import androidx.databinding.ktx.BuildConfig
import com.tolstoy.zurichat.data.localSource.StarredMessagesDatabase
import com.tolstoy.zurichat.data.repository.StarredMessagesRepository
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
