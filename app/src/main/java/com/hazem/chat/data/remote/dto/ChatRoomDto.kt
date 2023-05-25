package com.hazem.chat.data.remote.dto

import java.util.*

data class ChatRoomDto(
    val senderId: String = "",
    var userIChatWith: UserDto? = null,
    var lastMessage: String = "",
    var lastMessageDate: Date? = null
)
