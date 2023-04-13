package com.example.chat.di


import com.example.chat.data.local.dao.CountryDao
import com.example.chat.data.repository.CountryRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideCountryRepositoryImp(
        countryDao:CountryDao
    ):CountryRepositoryImp
    {


        return CountryRepositoryImp(countryDao)
    }
}