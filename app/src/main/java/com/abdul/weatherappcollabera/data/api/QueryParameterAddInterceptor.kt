package com.abdul.weatherappcollabera.data.api

import com.abdul.weatherappcollabera.common.AppConstants
import com.abdul.weatherappcollabera.permissions.ApplicationClass
import okhttp3.Interceptor
import okhttp3.Response

class QueryParameterAddInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", AppConstants.API_KEY)
            .addQueryParameter("appid", "")
            .build()

        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer token")
            .url(url)
            .build()

        return chain.proceed(request)
    }
}