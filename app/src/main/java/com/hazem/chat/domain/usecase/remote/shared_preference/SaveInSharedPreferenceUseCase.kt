package com.hazem.chat.domain.usecase.remote.shared_preference

import com.hazem.chat.domain.repository.local.LoginRepository
import javax.inject.Inject

class SaveInSharedPreferenceUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun <T> invoke(key: String, data: T) {
        loginRepository.saveInSharedPreference(key, data)
    }
}