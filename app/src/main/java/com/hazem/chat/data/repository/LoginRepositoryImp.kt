package com.hazem.chat.data.repository

import android.app.Activity
import android.util.Log

import com.hazem.chat.domain.repository.local.LoginRepository
import com.hazem.chat.utils.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LoginRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
) : LoginRepository {
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun sendOtpToPhone(
        phoneNumber: String,
        activity: Activity
    ): Flow<Resource<String, String>> = callbackFlow {

        callbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                    val otpCode = credential.smsCode

                    if (!otpCode.isNullOrEmpty()) {
                        Log.d("ErrorTest", otpCode)
                         trySend( Resource.Success("completed|$otpCode"))
                    }

                }

                override fun onVerificationFailed(firebaseException: FirebaseException) {
                    when (firebaseException) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            Log.d("ErrorTest", "onVerificationFailed: Invalid Phone Number")
                            trySend(Resource.Error("Invalid Phone Number"))
                        }

                        is FirebaseTooManyRequestsException -> {
                            Log.d(
                                "ErrorTest",
                                "onVerificationFailed: Too many attempts. Retry later."
                            )
                            trySend(Resource.Error("Too many attempts. Retry later."))
                        }

                        is FirebaseNetworkException -> {
//                        phoneCallbacksListener.onVerificationFailed("Network not available.")

                            val errorText = removeSurroundingString(firebaseException.message)
                            trySend(Resource.Error(errorText))
                            Log.d(
                                "ErrorTest",
                                errorText
                            )

                        }

                        else -> {
                            Log.d(
                                "ErrorTest",
                                "onVerificationFailed: ${firebaseException.message ?: ""}"
                            )
                            trySend(Resource.Error(firebaseException.message ?: ""))
                            // phoneCallbacksListener.onVerificationFailed("Unknown Error try later.")
                        }
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    _resendToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, _resendToken)
                    resendToken = _resendToken

                    trySend(Resource.Success(verificationId))
                }


            }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose {}

    }

    override suspend fun verifyOtpCode(otpCode: String,verificationID: String):Resource<String,String> {
        return signInWithPhoneAuthCredential(
            PhoneAuthProvider.getCredential(
                verificationID,
                otpCode
            )
        )
    }

    override suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Resource<String, String> {
        return try {
            auth.signInWithCredential(credential).await()
            Resource.Success("Logged in successfully")
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) {
        val resendCodeOptionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        resendCodeOptionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(resendCodeOptionsBuilder.build())
    }

    override suspend fun isUserVerified(): Boolean = auth.currentUser != null



    fun removeSurroundingString(str: String?): String {
        var start: Int? = null
        var end: Int? = null


        str!!.forEachIndexed { index, c ->
            if (c.toString() == "(") {
                start = index - 1
            }
            if (c.toString() == ")") {
                end = index + 1
            }
        }

        return if (start != null && end != null) {
            str.removeRange(start!!, end!!)
        } else {
            str
        }
    }

}