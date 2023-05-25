package com.hazem.chat.domain.repository.remote

import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getFirebaseCurrentUser(): FirebaseUser?
}