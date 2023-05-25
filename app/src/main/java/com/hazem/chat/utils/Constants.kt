package com.hazem.chat.utils

class Constants {
    companion object {

        //splash
        const val HANDLER_DELAY = 2000L

        //shared pref
        const val USER_ID_KEY = "user_id"
        const val USERNAME_KEY = "user_name"
        const val IS_LOGGED_IN_KEY = "is_logged_in"

        //time out
        const val TIMEOUT_UPLOAD = 12000L
        const val TIMEOUT = 10000L

        // Storage
        const val FIREBASE_STORAGE_ROOT_DIRECTORY = "app"
        //collection
        const val USER_FIRESTORE_COLLECTION= "users"
        const val MY_CHATS_FIIRESTORE_COLLECTION: String = "my_chats"
        const val MESSAGES_FIIRESTORE_COLLECTION: String = "Messages"
        const val CHATS_FIIRESTORE_COLLECTION: String = "Chats"
        const val TOKENS_FIIRESTORE_COLLECTION: String = "Tokens"
        const val NOTIFICATION_CHANNEL_ID: String = "channel_id"
        //notification
        const val FCM_API_KEY: String =
            "AAAA92L23Rs:APA91bEe5qTrNsAsrLti3mPkXCcyF3bemZwh4vSjAypuEZhSbPihuYI6c3gCAtuaRSyTfSiBrPeeTeTuOEoImvV3YCfMB2c1cIAEkFzmh4kkxHI49gjwFhw5B2uucpPFXEj03iRqQy3v"
        const val FCM_BASE_URL: String = "https://fcm.googleapis.com/"
    }
}