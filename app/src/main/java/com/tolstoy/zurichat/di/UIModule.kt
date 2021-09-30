package com.tolstoy.zurichat.di

import com.tolstoy.zurichat.util.ProgressLoader
import com.tolstoy.zurichat.util.ProgressLoaderImpl
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
