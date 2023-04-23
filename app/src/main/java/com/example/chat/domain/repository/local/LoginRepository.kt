package com.example.chat.domain.repository.local
import com.example.chat.utils.Resource

interface LoginRepository {
    fun isValidNumber(): Resource<Boolean,String>
}