package com.hazem.chat.data.remote.dto

import java.util.*

data class MessageDto (
    val messageId: String="",
    val senderId: String="",
    val receiverId: String="",
    val imageUrl: String? = null,
    val messageText: String? = null,
    val date: Date?=null,
        )