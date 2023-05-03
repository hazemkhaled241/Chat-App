package com.hazem.chat.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hazem.chat.data.local.dao.CountryDao
import com.hazem.chat.data.repository.CountryRepositoryImp
import com.hazem.chat.data.repository.LoginRepositoryImp
import com.hazem.chat.domain.repository.local.CountryRepository
import com.hazem.chat.domain.repository.local.LoginRepository
import com.hazem.chat.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCountryRepositoryImp(
        countryDao: CountryDao
    ): CountryRepository {
        return CountryRepositoryImp(countryDao)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        dataBase: FirebaseDatabase,
        auth: FirebaseAuth,
        sharedPrefs: SharedPrefs,
    ): LoginRepository {
        return LoginRepositoryImp(
            auth,
            dataBase,
            sharedPrefs
        )
    }
}