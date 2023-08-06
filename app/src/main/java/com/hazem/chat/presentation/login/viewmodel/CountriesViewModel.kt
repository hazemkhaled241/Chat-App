package com.hazem.chat.presentation.login.viewmodel

import androidx.lifecycle.*
import com.hazem.chat.data.local.Country
import com.hazem.chat.domain.usecase.local.GetAllCountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getAllCountriesUseCase: GetAllCountriesUseCase
) : ViewModel() {
    private val _countryState = MutableStateFlow<List<Country>?>(null)
    val countryState: StateFlow<List<Country>?> get() = _countryState
    fun fetchAllCountries() {
        viewModelScope.launch {
            _countryState.value = getAllCountriesUseCase.invoke()
        }
    }



}