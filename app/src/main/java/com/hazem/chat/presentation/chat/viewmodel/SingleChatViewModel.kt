package com.hazem.chat.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import com.hazem.chat.domain.usecase.remote.shared_preference.GetFromSharedPreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val getFromSharedPreferenceUseCase: GetFromSharedPreferenceUseCase
) :ViewModel() {

    fun <T> readFromSP(key: String, clazz: Class<T>): T {
        return getFromSharedPreferenceUseCase(key = key, clazz = clazz)
    }
}