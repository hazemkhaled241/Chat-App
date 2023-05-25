package com.hazem.chat.domain.usecase.remote.chat

import com.hazem.chat.data.repository.ChatRepositoryImp
import com.hazem.chat.domain.model.Contact
import com.hazem.chat.domain.model.User
import com.hazem.chat.utils.Resource
import javax.inject.Inject

class GetRegisteredContactUseCase @Inject constructor(
    private val chatRepositoryImp: ChatRepositoryImp
) {
    suspend operator fun invoke(contacts:ArrayList<Contact>):Resource<ArrayList<User>,String>{
       return chatRepositoryImp.getRegisteredContactsSuccessfully(contacts)
    }
}