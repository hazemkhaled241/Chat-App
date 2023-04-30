package com.hazem.chat.data.repository

import com.hazem.chat.data.local.Country
import com.hazem.chat.data.local.dao.CountryDao
import com.hazem.chat.domain.repository.local.CountryRepository
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