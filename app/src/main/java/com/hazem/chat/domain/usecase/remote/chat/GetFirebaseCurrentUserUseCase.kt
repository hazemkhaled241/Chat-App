package com.hazem.chat.domain.usecase.remote.chat

import com.google.firebase.auth.FirebaseUser
import com.hazem.chat.domain.repository.remote.UserRepository
import javax.inject.Inject

class GetFirebaseCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): FirebaseUser? {
        return userRepository.getFirebaseCurrentUser()
    }
}