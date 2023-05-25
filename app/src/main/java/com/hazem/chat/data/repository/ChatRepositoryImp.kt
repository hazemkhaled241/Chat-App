package com.hazem.chat.data.repository

import android.net.Uri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import com.hazem.chat.data.mapper.toMessage
import com.hazem.chat.data.mapper.toMessageDto
import com.hazem.chat.data.mapper.toUser
import com.hazem.chat.data.remote.FCMApiService
import com.hazem.chat.data.remote.dto.*
import com.hazem.chat.domain.model.Contact
import com.hazem.chat.domain.model.Message
import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.repository.remote.ChatRepository
import com.hazem.chat.utils.Constants
import com.hazem.chat.utils.Constants.Companion.CHATS_FIIRESTORE_COLLECTION
import com.hazem.chat.utils.Constants.Companion.MESSAGES_FIIRESTORE_COLLECTION
import com.hazem.chat.utils.Constants.Companion.USER_FIRESTORE_COLLECTION
import com.hazem.chat.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ChatRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference,
    private val fcmApiService: FCMApiService
):ChatRepository {
    override suspend fun sendMessage(message: Message): Resource<Message, String> {
        return withTimeout(Constants.TIMEOUT_UPLOAD) {
            try {
                uploadMessage(message)
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }
override suspend fun getRegisteredContactsSuccessfully(contacts:ArrayList<Contact>):Resource<ArrayList<User>,String>{
    return try {
        withTimeout(Constants.TIMEOUT) {

            delay(500) // to show loading progress
            val querySnapshot = firestore.collection(USER_FIRESTORE_COLLECTION)
                .get()
                .await()
            val usersDto = arrayListOf<UserDto>()
            for (document in querySnapshot) {
                val userDto = document.toObject(UserDto::class.java)
                usersDto.add(userDto)
            }
            Resource.Success(
                usersDto.filter {  p1 ->
                    contacts.any { p2 -> p1.phoneNumber == p2.number } }
                    .map { it.toUser() }
                    .toCollection(ArrayList())
            )
        }
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }
}
    private suspend fun uploadMessage(
        message: Message
    ): Resource<Message, String> {
        val chatsDocumentReference = firestore.collection(MESSAGES_FIIRESTORE_COLLECTION).document()
        message.messageId = chatsDocumentReference.id
        return if (message.imageUrl != null) {
            uploadImageMessage(message, chatsDocumentReference)
        } else { // text message
            uploadTextMessage(message, chatsDocumentReference)
        }
    }

    private suspend fun uploadTextMessage(
        message: Message,
        chatsDocumentReference: DocumentReference
    ): Resource<Message, String> {
        chatsDocumentReference.set(
            message.toMessageDto(),
            SetOptions.merge()
        ).await()
        // add receiver to sender chat list
        val senderChatListDocumentReference =
            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.senderId)
        val documentSnapshot = senderChatListDocumentReference.get().await()

        if (documentSnapshot.exists()) {
            var ids = documentSnapshot.get("ids") as? ArrayList<String>
            if (ids != null) {
                // Array already exists, add the new id if it's not there
                if (!ids.contains(message.receiverId))
                    ids.add(message.receiverId)
            } else {
                // Array does not exist, create a new array with the first id
                ids = arrayListOf(message.receiverId)
            }

            // Update the document with the modified or new array
            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.senderId).set(hashMapOf("ids" to ids)).await()
        } else { // first time to chat with someone

            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.senderId).set(hashMapOf("ids" to message.receiverId))
                .await()
        }

        // add sender to receiver chat list
        val receiverChatListDocumentReference =
            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.receiverId)
        val documentSnapshot2 = receiverChatListDocumentReference.get().await()
        if (documentSnapshot2.exists()) {
            var ids = documentSnapshot2.get("ids") as? ArrayList<String>
            if (ids != null) {
                // Array already exists, add the new id if it's not there
                if (!ids.contains(message.senderId))
                    ids.add(message.senderId)
            } else {
                // Array does not exist, create a new array with the first id
                val newItem = message.senderId
                val newArray = arrayListOf(newItem)
                ids = newArray
            }
            // Update the document with the modified or new array
            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.receiverId).set(hashMapOf("ids" to ids)).await()
        } else { // first time to chat with someone
            firestore.collection(CHATS_FIIRESTORE_COLLECTION)
                .document(message.receiverId).set(hashMapOf("ids" to message.senderId))
                .await()
        }

        val senderUserDocumentSnapshot =
            firestore.collection(USER_FIRESTORE_COLLECTION).document(message.senderId).get()
                .await()
        if (senderUserDocumentSnapshot.exists()) {
            val userDto = senderUserDocumentSnapshot.toObject(UserDto::class.java)
            // send notification
            sendNotification(userDto!!.name, message)
        }

        return Resource.Success(message)
    }

    private suspend fun uploadImageMessage(
        message: Message,
        chatsDocumentReference: DocumentReference
    ): Resource<Message, String> {
        return when (
            val result =
                uploadImageToStorage(message.senderId, message.imageUrl!!)
        ) {
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Success -> {
                message.imageUrl = result.data
                chatsDocumentReference.set(
                    message.toMessageDto(),
                    SetOptions.merge()
                ).await()

                val senderUserDocumentSnapshot =
                    firestore.collection(USER_FIRESTORE_COLLECTION).document(message.senderId)
                        .get()
                        .await()
                if (senderUserDocumentSnapshot.exists()) {
                    val user = senderUserDocumentSnapshot.toObject(UserDto::class.java)
                    // send notification
                    sendNotification(user!!.name, message)
                }

                Resource.Success(message)
            }
        }
    }

    private suspend fun sendNotification(username: String, message: Message) {
        val receiverTokenDocumentSnapshot =
            firestore.collection(Constants.TOKENS_FIIRESTORE_COLLECTION)
                .document(message.receiverId)
                .get()
                .await()
        if (receiverTokenDocumentSnapshot.exists()) {
            val token = receiverTokenDocumentSnapshot.get("token").toString()

            val fcmMessageDto = FCMMessageDto(
                title = "New Message",
                body = "$username: ${message.messageText}",
                senderId = message.senderId,
                receiverId = message.receiverId,
                image = message.imageUrl.toString()
            )
            val notificationDto = NotificationDto(
                fcmMessageDto = fcmMessageDto,
                token = token
            )
            fcmApiService.sendNotification(notificationDto = notificationDto)
        }
    }

//    private fun convertImageUrlToBitmap(image: String): Bitmap {
//        val url = URL(image)
//        return BitmapFactory.decodeStream(url.openConnection().getInputStream())
//    }

    override fun getMessages(
        senderId: String,
        receiverId: String
    ): Flow<Resource<List<Message>, String>> = callbackFlow {
        val chatsCollectionReference = firestore.collection(MESSAGES_FIIRESTORE_COLLECTION)
        chatsCollectionReference.orderBy("date")
            .addSnapshotListener { snapShots, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message.toString()))
                    return@addSnapshotListener
                }
                if (snapShots != null) {
                    val messageList = arrayListOf<MessageDto>()
                    for (snapShot in snapShots) {
                        val messageDto = snapShot.toObject(MessageDto::class.java)
                        if (
                            (messageDto.senderId == senderId && messageDto.receiverId == receiverId) ||
                            (messageDto.senderId == receiverId && messageDto.receiverId == senderId)
                        ) // first check for messages i send and the second is for messages i recieved from that user
                            messageList.add(messageDto)
                    }
                    trySend(Resource.Success(messageList.map { it.toMessage() }))
                }
            }

        awaitClose { }
    }

    private suspend fun uploadImageToStorage(
        ownerId: String,
        imageUri: Uri
    ): Resource<Uri, String> {
        return try {
            withTimeout(Constants.TIMEOUT_UPLOAD) {
                val uri: Uri = withContext(Dispatchers.IO) {
                    storageReference.child(
                        "$ownerId/${imageUri.lastPathSegment ?: System.currentTimeMillis()}"
                    )
                        .putFile(imageUri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                }
                Resource.Success(uri)
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}