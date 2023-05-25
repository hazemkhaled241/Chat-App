package com.hazem.chat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hazem.chat.domain.repository.remote.UserRepository
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
)
    :UserRepository {

    override fun getFirebaseCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}