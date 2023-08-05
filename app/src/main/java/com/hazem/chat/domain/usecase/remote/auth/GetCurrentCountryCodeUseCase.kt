package com.hazem.chat.domain.usecase.remote.auth

import com.hazem.chat.data.local.Country
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class GetCurrentCountryCodeUseCase @Inject constructor() {
    operator fun invoke(code:String,countries:ArrayList<Country>):Resource<Country,String>{
        return   try {
            Resource.Success( countries.first {
                 it.code.equals(code, true)
             }
            )
         }
         catch (e:Exception){
              Resource.Error(e.message.toString())
         }


    }
}