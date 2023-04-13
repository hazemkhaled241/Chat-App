package com.example.chat.domain.repository.local

import com.example.chat.data.local.Country

interface CountryRepository {
suspend fun insertCountry(country: Country)

 suspend fun getAllCountries(): List<Country>
}