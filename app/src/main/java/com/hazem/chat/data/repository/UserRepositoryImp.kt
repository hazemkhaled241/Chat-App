package com.hazem.chat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.repository.remote.UserRepository
import com.hazem.chat.utils.Constants
import com.hazem.chat.utils.Constants.Companion.USER_FIRESTORE_COLLECTION
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
    )
    :UserRepository {

    override fun getFirebaseCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun updateProfile(userId: String,user:User): Resource<String, String> {
        return try {
            withTimeout(Constants.TIMEOUT) {
                fireStore.collection(USER_FIRESTORE_COLLECTION).document(userId)
                    .update("name", user.name)
                    .await()
                Resource.Success("Updated successfully")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }
}