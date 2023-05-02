package com.hazem.chat.domain.model

import android.net.Uri

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String="",
    var imageUrl: Uri? = null,
    val time: Long = 0,
    val messageText: String = "",
    //val date: Date
)
