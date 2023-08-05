package com.hazem.chat.data.mapper

import android.net.Uri
import com.hazem.chat.data.remote.dto.MessageDto
import com.hazem.chat.data.remote.dto.UserDto
import com.hazem.chat.domain.model.Message
import com.hazem.chat.domain.model.User

fun User.toUserDto(): UserDto {
    return UserDto(
        userId = this.userId,
        phoneNumber = this.phoneNumber,
        name = this.name,
        url = this.uri.toString()
    )
}

fun UserDto.toUser(): User {
    return User(
        userId = this.userId,
        phoneNumber = this.phoneNumber,
        name = this.name,
        uri = Uri.parse(this.url)
    )
}
fun MessageDto.toMessage(): Message {
    val image = if (imageUrl == null) imageUrl else Uri.parse(imageUrl)
    return Message(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        messageText = messageText,
        imageUrl = image,
        date = date!!
    )
}

fun Message.toMessageDto(): MessageDto {
    val image = if (imageUrl == null) null else imageUrl.toString()
    return MessageDto(
        messageId = messageId,
        senderId = senderId,
        receiverId = receiverId,
        messageText = messageText,
        imageUrl = image,
        date = date,
    )
}