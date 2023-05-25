package com.hazem.chat.domain.usecase.remote.shared_preference

import com.hazem.chat.domain.repository.remote.LoginRepository
import javax.inject.Inject

class GetFromSharedPreferenceUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun <T> invoke(key: String, clazz: Class<T>): T {
        return loginRepository.getFromSharedPreference(key, clazz)
    }
}