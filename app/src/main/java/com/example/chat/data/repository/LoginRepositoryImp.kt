package com.example.chat.data.repository

import com.example.chat.domain.repository.local.LoginRepository
import com.example.chat.utils.Resource

class LoginRepositoryImp :LoginRepository {
    override fun isValidNumber(): Resource<Boolean, String> {
        TODO("Not yet implemented")
    }

}