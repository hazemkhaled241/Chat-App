package com.hazem.chat.domain.usecase.remote.auth
import android.app.Activity
import com.hazem.chat.data.local.Country
import com.hazem.chat.utils.Resource
import com.hazem.chat.data.repository.LoginRepositoryImp
import com.hazem.chat.utils.isValidNumber
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class IsValidNumberUseCase @Inject constructor(
    private val loginRepositoryImp: LoginRepositoryImp
) {
    operator fun invoke(
        country: Country,
        phoneNumber: String,
        activity: Activity
    ): Flow<Resource<String, String>> =callbackFlow {
        val phone=country.noCode + phoneNumber.substring(
            1,
            phoneNumber.length
        )
            when(val result = isValidNumber(phoneNumber,  country.code)) {
                is Resource.Error -> trySend(Resource.Error(result.message))
                is Resource.Success -> {
                    loginRepositoryImp.sendOtpToPhone(phone, activity
                ).collect {
                    when (it) {
                        is Resource.Error -> trySend(Resource.Error(it.message))
                        is Resource.Success -> trySend(Resource.Success(it.data))
                    }
                }
            }
            }



         awaitClose {}
         }

    }

