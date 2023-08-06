package com.hazem.chat.domain.usecase.remote.auth

import com.hazem.chat.domain.repository.remote.LoginRepository
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class SaveUserDateUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(userName: String, uri: android.net.Uri):Resource<String,String>{
        return loginRepository.saveUserData(userName,uri)
    }
}