package com.example.chat.domain.repository.local

import com.example.chat.data.local.Country
import com.example.chat.utils.Resource

interface CountryRepository {
suspend fun insertCountry(country: Country)
suspend fun getAllCountry(): Resource<ArrayList<Country>, String>
}