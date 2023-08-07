package com.hazem.chat.presentation.profile.viewmodel



sealed class UpdateProfileState{
    object Init : UpdateProfileState()
    data class IsLoading(val isLoading: Boolean) : UpdateProfileState()
    data class ShowError(val message: String) : UpdateProfileState()
    data class UpdatedSuccessfully(val message: String) : UpdateProfileState()
}
