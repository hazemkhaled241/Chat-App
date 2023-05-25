package com.hazem.chat.domain.model

import java.util.*

data class ChatRoom(
    val senderId: String,
    val userIChatWith: User,
    val lastMessage: String,
    val lastMessageDate: Date
)
