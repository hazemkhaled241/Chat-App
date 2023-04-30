package com.hazem.chat.domain.repository.local

import android.app.Activity
import com.google.firebase.auth.PhoneAuthCredential
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun isUserVerified(): Boolean
   /* suspend fun resendOtpCode(phoneNumber: String, activity: Activity)*/
     fun sendOtpToPhone(phoneNumber: String, activity: Activity): Flow<Resource<String, String>>
    suspend fun verifyOtpCode(otpCode: String,verificationID: String):Resource<String,String>
    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Resource<String, String>
}