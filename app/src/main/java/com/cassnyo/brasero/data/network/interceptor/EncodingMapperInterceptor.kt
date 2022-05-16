package com.cassnyo.brasero.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * AEMET OpenData returns its data encoded to ISO-8859-15. This interceptor will transform it to UTF-8
 */
class EncodingMapperInterceptor : Interceptor {

    companion object {
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE = "application/json;charset=UTF-8"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val newBody = originalResponse.body?.source()
            ?.readString(Charsets.ISO_8859_1)
            ?.toResponseBody(CONTENT_TYPE .toMediaType())
        return originalResponse.newBuilder()
            .header(HEADER_CONTENT_TYPE, CONTENT_TYPE)
            .body(newBody)
            .build()
    }
}