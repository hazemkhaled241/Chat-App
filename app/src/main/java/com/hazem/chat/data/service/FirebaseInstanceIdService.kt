package com.hazem.chat.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.hazem.chat.utils.Constants.Companion.TOKENS_FIIRESTORE_COLLECTION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseInstanceIdService : FirebaseMessagingService() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var fcm: FirebaseMessaging

    // Override onNewToken to get new token then save it in firestore for later usage
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fcm.token.addOnSuccessListener { token ->
                // update token
                firestore.collection(TOKENS_FIIRESTORE_COLLECTION).document(currentUser.uid)
                    .set(hashMapOf("token" to token))
            }.addOnFailureListener { e ->
            }
        }
    }
}