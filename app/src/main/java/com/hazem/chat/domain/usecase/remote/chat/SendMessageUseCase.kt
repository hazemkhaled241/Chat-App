package com.hazem.chat.domain.usecase.remote.chat

import com.hazem.chat.data.repository.ChatRepositoryImp
import com.hazem.chat.domain.model.Message
import com.hazem.chat.utils.Resource
import com.hazem.chat.utils.isValidMessage
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    val chatRepositoryImp: ChatRepositoryImp
) {
    suspend operator fun invoke(message: Message): Resource<Message, String> {
        return when (message.isValidMessage()) {
            true -> chatRepositoryImp.sendMessage(message)
            false -> Resource.Error("message_cannot_be_empty")
        }
    }
}