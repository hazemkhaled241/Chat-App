package com.hazem.chat.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.data.local.Country
import com.hazem.chat.domain.usecase.local.InsertCountryUseCase
import com.hazem.chat.domain.usecase.remote.shared_preference.GetFromSharedPreferenceUseCase
import com.hazem.chat.domain.usecase.remote.shared_preference.SaveInSharedPreferenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getFromSharedPreferenceUseCase: GetFromSharedPreferenceUseCase,
    private val insertCountryUseCase: InsertCountryUseCase,
    private val  saveInSharedPreferenceUseCase: SaveInSharedPreferenceUseCase
) : ViewModel() {


    fun saveInSP(key: String, value: Boolean) {
        saveInSharedPreferenceUseCase(key, value)
    }

    fun <T> getFromSP(key: String, clazz: Class<T>): T {
        return getFromSharedPreferenceUseCase(key, clazz)
    }
    fun insertCountry(country: Country) {
        viewModelScope.launch {
            insertCountryUseCase.invoke(country)
        }
    }
}