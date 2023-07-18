package com.hazem.chat.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.hazem.chat.utils.Constants.Companion.NOT_IN_MYCHATS_OR_CHATROOM
import com.hazem.chat.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@AndroidEntryPoint
open class FirebaseMessagingService :FirebaseMessagingService()  {

    @Inject
    lateinit var sharedPreferences: SharedPrefs

    override fun handleIntent(intent: Intent) {
        val bundle = intent.extras ?: return
        /*if (bundle != null) {
            for (key in bundle.keySet()) { // to know the keys and values of the sent message
                val value = bundle.getString(key)
            }
        }*/

        if (!sharedPreferences.get(NOT_IN_MYCHATS_OR_CHATROOM, Boolean::class.java)) return

        val body = bundle.getString("gcm.notification.body")
        val image = bundle.getString("gcm.notification.image")
        val title = bundle.getString("gcm.notification.title")
        val senderId = bundle.getString("gcm.notification.senderId")
        if (title != null && body != null && image != null && senderId != null)
            sendNotification(title, body, image, senderId)
        super.handleIntent(intent)
    }

    private fun sendNotification(title: String, body: String, image: String, senderId: String) {
        val bundle = Bundle().apply {
            putString(
                "userId",
                senderId
            )
        }
        val pendingIntent = createPendingIntent(bundle, R.id.singleChatFragment)





        // Display notification
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        /**
         * todo: ask for notification permission start from android 13
         **/
        // Since android Oreo (Api:26) notification channel is needed.
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            title,
            NotificationManager.IMPORTANCE_HIGH
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
            notificationManager.notify(Date().time.toInt(), notificationBuilder.build())
        }
    }

    private fun createPendingIntent(bundle: Bundle, fragmentId: Int): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setComponentName(MainActivity::class.java)
            .setDestination(fragmentId)
            .setArguments(
                bundle
            ) // replace with deep links to maintain back stack
            .createPendingIntent()
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

                    notificationBuilder.setStyle(bigPictureStyle)

                    notificationManager.createNotificationChannel(channel)
                    notificationManager.notify(
                        Date().time.toInt(),
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