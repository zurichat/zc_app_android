package com.tolstoy.zurichat

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import androidx.databinding.ktx.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class ZuriChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
