package com.hazem.chat.domain.repository.local

import com.hazem.chat.data.local.Country

interface CountryRepository {
suspend fun insertCountry(country: Country)

 suspend fun getAllCountries(): List<Country>
}