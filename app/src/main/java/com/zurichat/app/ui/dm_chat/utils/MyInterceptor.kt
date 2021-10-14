package com.zurichat.app.ui.dm_chat.utils

import androidx.core.content.ContentProviderCompat.requireContext
import com.zurichat.app.models.User
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.util.jsearch_view_utils.scanForActivity
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer")
            .build()
        return chain.proceed(request)
    }

}