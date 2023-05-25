package com.hazem.chat.presentation.chat.viewmodel

import com.hazem.chat.domain.model.Message

sealed class SingleChatState {
    object Init : SingleChatState()
    data class IsLoading(val isLoading: Boolean) : SingleChatState()
    data class ShowError(val message: String) : SingleChatState()
    data class FetchMessagesSuccessfullySuccessfully(val messages: List<Message>) :
        SingleChatState()
    data class SendMessageSuccessfully(val message: Message) : SingleChatState()

}
