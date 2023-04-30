package com.hazem.chat.domain.usecase
import android.app.Activity
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
        code: String,
        phoneNumber: String,
        activity: Activity
    ): Flow<Resource<String, String>> =callbackFlow {

            when(val result = isValidNumber(phoneNumber,  code)){
                is Resource.Error -> trySend(Resource.Error(result.message))
                is Resource.Success -> loginRepositoryImp.sendOtpToPhone("+201116510283",activity).collect{
                    when(it){
                        is Resource.Error -> trySend(Resource.Error(it.message))
                        is Resource.Success ->trySend(Resource.Success(it.data))
                    }
                }
            }



         awaitClose {}
         }

    }

