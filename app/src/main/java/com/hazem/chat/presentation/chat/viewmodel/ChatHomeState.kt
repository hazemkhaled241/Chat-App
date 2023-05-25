package com.hazem.chat.presentation.chat.viewmodel

import com.hazem.chat.domain.model.User

sealed class ChatHomeState{
    object Init : ChatHomeState()
    data class IsLoading(val isLoading: Boolean) : ChatHomeState()
    data class ShowError(val message: String) : ChatHomeState()
    data class FetchAllRegisteredContactsSuccessfullySuccessfully(val messages: ArrayList<User>) :
        ChatHomeState()
}
