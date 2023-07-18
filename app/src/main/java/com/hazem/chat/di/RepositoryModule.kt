package com.hazem.chat.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import com.hazem.chat.data.local.dao.CountryDao
import com.hazem.chat.data.remote.FCMApiService
import com.hazem.chat.data.repository.ChatRepositoryImp
import com.hazem.chat.data.repository.CountryRepositoryImp
import com.hazem.chat.data.repository.LoginRepositoryImp
import com.hazem.chat.data.repository.UserRepositoryImp
import com.hazem.chat.domain.repository.local.CountryRepository
import com.hazem.chat.domain.repository.remote.ChatRepository
import com.hazem.chat.domain.repository.remote.LoginRepository
import com.hazem.chat.domain.repository.remote.UserRepository
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
        fireStore: FirebaseFirestore,
        auth: FirebaseAuth,
        sharedPrefs: SharedPrefs,
        fcm: FirebaseMessaging
    ): LoginRepository {
        return LoginRepositoryImp(
            auth,
            fireStore,
            sharedPrefs,
            fcm
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,

        ): UserRepository {
        return UserRepositoryImp(
            auth
        )
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        storageReference: StorageReference,
        fcmApiService: FCMApiService,
    ): ChatRepository {
        return ChatRepositoryImp(
            firestore,
            storageReference,
            fcmApiService
        )
    }
}