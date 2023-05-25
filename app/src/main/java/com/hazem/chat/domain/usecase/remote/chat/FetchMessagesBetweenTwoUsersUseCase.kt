package com.hazem.chat.domain.usecase.remote.chat

import com.hazem.chat.data.repository.ChatRepositoryImp
import com.hazem.chat.domain.model.Message
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMessagesBetweenTwoUsersUseCase @Inject constructor(
private val chatRepositoryImp: ChatRepositoryImp
) {
    operator fun invoke(
        senderId: String,
        recipientId: String
    ): Flow<Resource<List<Message>, String>> {
        return chatRepositoryImp.getMessages(senderId, recipientId)
    }
}