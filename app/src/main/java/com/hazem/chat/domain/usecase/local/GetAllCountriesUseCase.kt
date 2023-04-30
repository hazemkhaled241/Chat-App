package com.hazem.chat.domain.usecase.local

import com.hazem.chat.data.local.Country
import com.hazem.chat.data.repository.CountryRepositoryImp

import javax.inject.Inject

class GetAllCountriesUseCase@Inject constructor(
    private val countryRepositoryImp: CountryRepositoryImp
) {
    suspend operator fun invoke(): List<Country> {
       return countryRepositoryImp.getAllCountries()

    }
}