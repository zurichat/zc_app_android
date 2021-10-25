package com.zurichat.app

import android.app.Application
import androidx.databinding.ktx.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * The base application for
 * */
@HiltAndroidApp
class ZuriChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
