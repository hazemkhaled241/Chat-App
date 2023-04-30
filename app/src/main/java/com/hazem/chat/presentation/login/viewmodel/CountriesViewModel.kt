package com.hazem.chat.presentation.login.viewmodel

import androidx.lifecycle.*
import com.hazem.chat.data.local.Country
import com.hazem.chat.domain.usecase.local.GetAllCountriesUseCase
import com.hazem.chat.domain.usecase.local.InsertCountryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getAllCountriesUseCase: GetAllCountriesUseCase,
    private val insertCountryUseCase: InsertCountryUseCase
) : ViewModel() {
    private val _countryState = MutableStateFlow<List<Country>?>(null)
    val countryState: StateFlow<List<Country>?> get() = _countryState
    fun fetchAllCountries() {
        viewModelScope.launch {
            _countryState.value = getAllCountriesUseCase.invoke()
        }
    }
    fun insertCountry(country: Country) {
        viewModelScope.launch {
           insertCountryUseCase.invoke(country)
        }
    }


}