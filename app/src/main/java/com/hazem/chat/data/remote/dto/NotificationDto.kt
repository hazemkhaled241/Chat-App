package com.hazem.chat.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("notification") val fcmMessageDto: FCMMessageDto,
    @SerializedName("to") val token: String = ""
)