package com.hazem.chat.di

import com.hazem.chat.data.remote.FCMApiService
import com.hazem.chat.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideRetrofitInstance(): FCMApiService {
        return Retrofit.Builder().baseUrl(Constants.FCM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(FCMApiService::class.java)
    }
}