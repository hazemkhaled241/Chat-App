package com.hazem.chat.domain.usecase.remote.chat

import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.repository.remote.UserRepository
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String): Resource<User, String> {
        return if (id.isNotEmpty()) {
            userRepository.getUserById(id)
        } else {
            Resource.Error("user id is empty!")
        }
    }
}