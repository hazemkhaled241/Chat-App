package com.hazem.chat.domain.usecase.remote.auth

import com.hazem.chat.data.repository.LoginRepositoryImp
import com.hazem.chat.domain.model.User
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class VerificationUseCase @Inject constructor(
    private val loginRepositoryImp: LoginRepositoryImp
){
    suspend operator fun invoke(otpCode: String, verificationID: String, user: User):Resource<String,String>{
        return loginRepositoryImp.verifyOtpCode(otpCode,verificationID,user)
    }
}