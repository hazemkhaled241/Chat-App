package com.hazem.chat.data.remote

import com.hazem.chat.data.remote.dto.NotificationDto
import com.hazem.chat.utils.Constants
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMApiService {
    @Headers(
        "Authorization: key=${Constants.FCM_API_KEY}",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    suspend fun sendNotification(@Body notificationDto: NotificationDto): ResponseBody
}