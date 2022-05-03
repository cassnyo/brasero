package com.cassnyo.brasero.di.module

import com.cassnyo.brasero.BuildConfig
import com.cassnyo.brasero.data.network.AemetApi
import com.cassnyo.brasero.data.network.adapter.LocalDateTimeAdapter
import com.cassnyo.brasero.data.network.adapter.LocalTimeAdapter
import com.cassnyo.brasero.data.network.interceptor.ApiKeyInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(LocalDateTimeAdapter())
            .add(LocalTimeAdapter())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(
        moshi: Moshi
    ): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideAemetApi(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): AemetApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.AEMET_API_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

        return retrofit.create()
    }

}