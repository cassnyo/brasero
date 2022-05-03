package com.cassnyo.brasero.data.network.interceptor

import com.cassnyo.brasero.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val HEADER_API_KEY = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HEADER_API_KEY, BuildConfig.AEMET_API_KEY)
            .build()
        return chain.proceed(request)
    }

}