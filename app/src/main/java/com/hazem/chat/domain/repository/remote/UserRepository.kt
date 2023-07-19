package com.hazem.chat.domain.repository.remote

import com.google.firebase.auth.FirebaseUser
import com.hazem.chat.domain.model.User
import com.hazem.chat.utils.Resource

interface UserRepository {
    fun getFirebaseCurrentUser(): FirebaseUser?
    suspend fun updateProfile(userId: String,user:User): Resource<String, String>
}