package com.example.chat.data.repository

import com.example.chat.data.local.Country
import com.example.chat.domain.repository.local.CountryRepository
import com.example.chat.utils.Resource

class CountryRepositoryImp :CountryRepository{
    override suspend fun insertCountry(country: Country) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCountry(): Resource<ArrayList<Country>, String> {
        TODO("Not yet implemented")
    }
}