package com.tolstoy.zurichat.di

import com.tolstoy.zurichat.data.repository.ChannelRepository
import com.tolstoy.zurichat.data.repository.ChannelRepositoryImpl
import com.tolstoy.zurichat.data.repository.UserOrganizationRepository
import com.tolstoy.zurichat.data.repository.UserOrganizationRepositoryImpl
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
