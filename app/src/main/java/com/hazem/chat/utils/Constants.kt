package com.hazem.chat.utils

class Constants {
    companion object {

        //splash
        const val HANDLER_DELAY = 2000L

        //shared pref
        const val USER_ID_KEY = "user_id"
        const val USERNAME_KEY = "user_name"
        const val IS_LOGGED_IN_KEY = "is_logged_in"
        const val COUNTRIES_SAVED = "countries_saved"

        //time out
        const val TIMEOUT_UPLOAD = 12000L
        const val TIMEOUT = 10000L

        // Storage
        const val FIREBASE_STORAGE_ROOT_DIRECTORY = "app"

        //collection
        const val USER_FIRESTORE_COLLECTION = "users"
        const val MESSAGES_FIIRESTORE_COLLECTION: String = "Messages"
        const val CHATS_FIIRESTORE_COLLECTION: String = "Chats"
        const val TOKENS_FIIRESTORE_COLLECTION: String = "Tokens"
        const val NOTIFICATION_CHANNEL_ID: String = "channel_id"
        const val NOT_IN_MYCHATS_OR_CHATROOM = "inChatRoomOrMyChats"

        //notification
        const val FCM_API_KEY: String =
            "AAAAZ-LOeTo:APA91bHhY1koiH-09JZv7HWa9fXVn_aYzEBnKS6VVtNP6beOlJ8zCxujrhCXBNGjJUg-7tXw_pxd9JbwSF8VrlyKmf6LyVc9QH4i4vcayNi6INe9totOTsMD-on8pzx3tLbxh2MYYWqS"
        const val FCM_BASE_URL: String = "https://fcm.googleapis.com/"
    }
}