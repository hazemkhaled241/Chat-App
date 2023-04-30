package com.hazem.chat.presentation.verification.viewmodel

sealed class VerificationState{
    object Init : VerificationState()
    data class IsLoading(val isLoading: Boolean) : VerificationState()
    data class ShowError(val message: String) : VerificationState()
    data class LoginSuccessfully(val message: String) : VerificationState()
   // data class NoInternetConnection(val message: String) : VerificationState()
}
