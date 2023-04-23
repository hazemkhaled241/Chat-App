package com.example.chat.presentation.login.viewmodel

sealed class LoginState{
    object Init : LoginState()
    data class IsLoading(val isLoading: Boolean) : LoginState()
    data class ShowError(val message: String) : LoginState()
    data class ValidNumber(val message: String) : LoginState()
    //data class NoInternetConnection(val message: String) : LoginState()
}
