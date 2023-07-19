package com.hazem.chat.domain.usecase.update_profile

import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.repository.remote.UserRepository
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String,user:User):Resource<String   ,String>{
        return userRepository.updateProfile(userId,user)
    }
}