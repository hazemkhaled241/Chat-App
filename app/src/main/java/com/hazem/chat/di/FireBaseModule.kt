package com.hazem.chat.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hazem.chat.utils.Constants.Companion.FIREBASE_STORAGE_ROOT_DIRECTORY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireBaseModule {
    @Singleton
    @Provides
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageInstance(): StorageReference {
        return FirebaseStorage.getInstance().getReference(FIREBASE_STORAGE_ROOT_DIRECTORY)
    }

    @Singleton
    @Provides
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Singleton
    @Provides
    fun provideFirebaseCloudMessagingInstance(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}