package com.example.chat.domain.usecase.local

import com.example.chat.data.local.Country
import com.example.chat.data.repository.CountryRepositoryImp

import javax.inject.Inject

class GetAllCountriesUseCase@Inject constructor(
    private val countryRepositoryImp: CountryRepositoryImp
) {
    suspend operator fun invoke(): List<Country> {
       return countryRepositoryImp.getAllCountries()

    }
}