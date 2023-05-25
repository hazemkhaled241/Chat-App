package com.hazem.chat.domain.model

import android.net.Uri
import java.util.*

data class Message(
    var messageId: String,
    val senderId: String,
    val receiverId: String,
    var imageUrl: Uri? = null,
    val messageText: String? = null,
    val date: Date,
)
