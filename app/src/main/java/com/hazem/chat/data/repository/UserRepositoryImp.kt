package com.hazem.chat.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.hazem.chat.data.mapper.toUser
import com.hazem.chat.data.remote.dto.UserDto
import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.repository.remote.UserRepository
import com.hazem.chat.utils.Constants
import com.hazem.chat.utils.Constants.Companion.USER_FIRESTORE_COLLECTION
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storageReference: StorageReference
) : UserRepository {

    override fun getFirebaseCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun updateProfile(userId: String, user: User): Resource<String, String> {
        return try {
            withTimeout(Constants.TIMEOUT) {

                when (
                    val result =
                        uploadImageToStorage(
                            userId,
                            user.uri
                        )
                ) {
                    is Resource.Error -> Resource.Error(result.message)
                    is Resource.Success -> {
                        fireStore.collection(USER_FIRESTORE_COLLECTION).document(userId)
                            .update("name", user.name, "url", user.uri.toString())
                            .await()
                        Resource.Success("Updated successfully")
                    }
                }
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    private suspend fun uploadImageToStorage(
        userId: String,
        imageUri: Uri
    ): Resource<Uri, String> {
        return try {
            withTimeout(Constants.TIMEOUT_UPLOAD) {
                val uri: Uri = withContext(Dispatchers.IO) {
                    storageReference.child(
                        "$userId/${imageUri.lastPathSegment ?: System.currentTimeMillis()}"
                    )
                        .putFile(imageUri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                }
                Resource.Success(uri)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getUserById(id: String): Resource<User, String> {
        return try {
            withTimeout(Constants.TIMEOUT) {

                delay(500) // to show loading progress
                val querySnapshot = fireStore.collection(USER_FIRESTORE_COLLECTION).document(id)
                    .get()
                    .await()
                val userDto = querySnapshot.toObject(UserDto::class.java)
                Resource.Success(
                    data = userDto!!.toUser()
                )
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}