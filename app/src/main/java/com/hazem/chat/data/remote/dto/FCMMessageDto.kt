package com.hazem.chat.data.remote.dto

data class FCMMessageDto(
    val title: String = "",
    val body: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val image: String = ""
)