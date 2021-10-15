package com.zurichat.app.di

import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.ProgressLoaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface UIModule {

    @get:Binds
    val ProgressLoaderImpl.progressLoader: ProgressLoader
}
