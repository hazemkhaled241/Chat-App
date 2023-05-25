package com.hazem.chat.domain.repository.remote

import com.hazem.chat.domain.model.Contact
import com.hazem.chat.domain.model.Message
import com.hazem.chat.domain.model.User
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: Message): Resource<Message, String>
    fun getMessages(
        senderId: String,
        receiverId: String,
    ): Flow<Resource<List<Message>, String>>
    suspend fun getRegisteredContactsSuccessfully(contacts:ArrayList<Contact>):Resource<ArrayList<User>,String>
}