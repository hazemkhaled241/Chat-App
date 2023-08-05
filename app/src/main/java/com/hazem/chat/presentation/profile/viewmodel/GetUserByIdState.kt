package com.hazem.chat.presentation.profile.viewmodel

import com.hazem.chat.domain.model.User


sealed class GetUserByIdState{
    object Init : GetUserByIdState()
    data class ShowError(val message: String) : GetUserByIdState()
    data class GetUserByIdSuccessfully(val message: User) : GetUserByIdState()
}
