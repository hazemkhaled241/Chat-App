package com.hazem.chat.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import coil.ImageLoader
import coil.request.ImageRequest
import coil.target.Target
import com.google.firebase.messaging.RemoteMessage
import com.hazem.chat.MainActivity
import com.hazem.chat.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.hazem.chat.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class FirebaseMessagingService :FirebaseMessagingService()  {

    override fun handleIntent(intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle[key]
            }
        }

        if (bundle == null) return

        val body = bundle["gcm.notification.body"] as String
        val image = bundle["gcm.notification.image"] as String
        val title: String = bundle["gcm.notification.title"] as String
        val senderId: String = bundle["gcm.notification.senderId"] as String
        sendNotification(title, body, image, senderId)
        super.handleIntent(intent)
    }

    private fun sendNotification(title: String, body: String, image: String, senderId: String) {

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(R.id.singleChatFragment)
            .setArguments(Bundle().apply {
                putString(
                    "userId",
                    senderId
                )
            }) // replace with deep links to maintain back stack
            .createPendingIntent()
        // Display notification
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        /**
         * todo: ask for notification permission start from android 13
         **/
        // Since android Oreo (Api:26) notification channel is needed.
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            title,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        if (image != "null") {
            setBigPictureFromBitmap(
                notificationBuilder,
                image,
                notificationManager,
                channel
            )
        } else {
            // we use the CompletableFuture  instance to wait until we add the large icon then we sent the notification
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(Random.nextInt(), notificationBuilder.build())
        }
    }

    private fun setBigPictureFromBitmap(
        notificationBuilder: NotificationCompat.Builder,
        image: String,
        notificationManager: NotificationManager,
        channel: NotificationChannel
    ) {
        val request = ImageRequest.Builder(this)
            .data(image)
            .target(object : Target {
                override fun onSuccess(result: Drawable) {
                    val bitmap = (result as BitmapDrawable).bitmap
                    // Set the loaded bitmap as the big picture
                    val bigPictureStyle = NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null)

                    notificationBuilder.setStyle(bigPictureStyle)

                    notificationManager.createNotificationChannel(channel)
                    notificationManager.notify(
                        0,
                        notificationBuilder.build()
                    )
                }
            }).build()
        // Start the image request
        ImageLoader(this).enqueue(request)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

    }
}