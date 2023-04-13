package com.example.chat.data.repository

import com.example.chat.data.local.Country
import com.example.chat.data.local.dao.CountryDao
import com.example.chat.domain.repository.local.CountryRepository
import javax.inject.Inject

class CountryRepositoryImp @Inject constructor(
   private val countryDao:CountryDao
) :CountryRepository{
    override suspend fun insertCountry(country: Country) {
        countryDao.insert(country)
    }

    override suspend fun getAllCountries():List<Country> {
        return try {
            countryDao.getAllCountry()

       }
       catch (e:Exception){
       throw e
       }


    }
}