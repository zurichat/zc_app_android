package com.zurichat.app.di

import com.zurichat.app.data.repository.ChannelRepository
import com.zurichat.app.data.repository.ChannelRepositoryImpl
import com.zurichat.app.data.repository.UserOrganizationRepository
import com.zurichat.app.data.repository.UserOrganizationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @get:Binds
    val ChannelRepositoryImpl.createChannelRepository: ChannelRepository

    @get:Binds
    val UserOrganizationRepositoryImpl.getUserOrganizationRepository: UserOrganizationRepository
}
