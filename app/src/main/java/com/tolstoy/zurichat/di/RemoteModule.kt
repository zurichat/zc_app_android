package com.tolstoy.zurichat.di

import com.tolstoy.zurichat.data.remoteSource.*
import com.tolstoy.zurichat.ui.newchannel.CreateChannelRemote
import com.tolstoy.zurichat.ui.newchannel.RetrofitChannelClient
import com.tolstoy.zurichat.ui.newchannel.remote.CreateChannelRemoteImpl
import com.tolstoy.zurichat.ui.organizations.remote.UserOrganizationRemote
import com.tolstoy.zurichat.ui.organizations.remote.UserOrganizationRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {
    @get:Binds
    val CreateChannelRemoteImpl.createChannelRepositoryRemote: CreateChannelRemote

    @get:Binds
    val UserOrganizationRemoteImpl.getUserOrganizationRemote: UserOrganizationRemote

    companion object {
        @[Provides Singleton]
        fun provideApiService(tokenInterceptor: TokenInterceptor): NewChannelApiService =
            ApiServiceFactory.createApiService(isDebug = true, tokenInterceptor = tokenInterceptor)

        @ChannelRetrofitService
        @[Provides Singleton]
        fun provideRetrofitChannel(): UsersService =
            RetrofitChannelClient.getRetrofitService()


        @[Provides Singleton]
        fun provideRetrofitOrganization(tokenInterceptor: TokenInterceptor): OrganizationService =
            OrganizationRetrofitClient.createOrganizationApiService(
                isDebug = true,
                tokenInterceptor = tokenInterceptor
            )

    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChannelRetrofitService