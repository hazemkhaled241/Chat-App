package com.hazem.chat.domain.usecase

import com.hazem.chat.data.repository.LoginRepositoryImp
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class VerificationUseCase @Inject constructor(
    private val loginRepositoryImp: LoginRepositoryImp
){
    suspend operator fun invoke(otpCode:String,verificationID: String):Resource<String,String>{
        return loginRepositoryImp.verifyOtpCode(otpCode,verificationID)
    }
}