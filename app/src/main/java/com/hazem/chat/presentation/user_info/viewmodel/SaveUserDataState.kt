package com.hazem.chat.presentation.user_info.viewmodel


sealed class SaveUserDataState {
    object Init : SaveUserDataState()
    data class IsLoading(val isLoading: Boolean) : SaveUserDataState()
    data class ShowError(val message: String) : SaveUserDataState()
    data class SavedSuccessfully(val message: String) : SaveUserDataState()
}