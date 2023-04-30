package com.hazem.chat.presentation.login.viewmodel

import com.hazem.chat.presentation.verification.viewmodel.VerificationState

sealed class LoginState{
    object Init : LoginState()
    data class IsLoading(val isLoading: Boolean) : LoginState()
    data class ShowError(val message: String) : LoginState()
    data class ValidNumber(val message: String) : LoginState()
   // data class SmsArrivedSuccessfully(val message: String) : LoginState()

    //data class NoInternetConnection(val message: String) : LoginState()
}
