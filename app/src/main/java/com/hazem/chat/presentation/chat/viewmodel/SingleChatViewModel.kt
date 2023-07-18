package com.hazem.chat.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.hazem.chat.domain.model.Message
import com.hazem.chat.domain.usecase.remote.chat.FetchMessagesBetweenTwoUsersUseCase
import com.hazem.chat.domain.usecase.remote.chat.GetFirebaseCurrentUserUseCase
import com.hazem.chat.domain.usecase.remote.chat.SendMessageUseCase
import com.hazem.chat.domain.usecase.remote.shared_preference.GetFromSharedPreferenceUseCase
import com.hazem.chat.domain.usecase.remote.shared_preference.SaveInSharedPreferenceUseCase
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val getFromSharedPreferenceUseCase: GetFromSharedPreferenceUseCase,
    private val fetchMessagesBetweenTwoUsersUseCase: FetchMessagesBetweenTwoUsersUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getFirebaseCurrentUserUseCase: GetFirebaseCurrentUserUseCase,
    private val saveInSharedPreferenceUseCase: SaveInSharedPreferenceUseCase

) :ViewModel() {
    private var _chatRoomState = MutableStateFlow<SingleChatState>(SingleChatState.Init)
    val chatRoomState = _chatRoomState.asStateFlow()
    var scroll=true

    fun requestSendMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessageUseCase(message).let {
                when (it) {
                    is Resource.Error -> showError(it.message)
                    is Resource.Success -> {
                        _chatRoomState.value =
                            SingleChatState.SendMessageSuccessfully(it.data)
                    }
                }
            }
        }
    }

    fun requestFetchMessagesBetweenTwoUsers(
        senderId: String,
        receiverId: String
    ) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            fetchMessagesBetweenTwoUsersUseCase(senderId, receiverId).collect {
                when (it) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            showError(it.message)
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _chatRoomState.value =
                                SingleChatState.FetchMessagesSuccessfullySuccessfully(it.data)
                        }
                    }
                }
            }
        }
    }

    fun getFirebaseCurrentUser(): FirebaseUser? {
        return getFirebaseCurrentUserUseCase()
    }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _chatRoomState.value = SingleChatState.IsLoading(true)
            }
            false -> {
                _chatRoomState.value = SingleChatState.IsLoading(false)
            }
        }
    }

    private fun showError(message: String) {

                _chatRoomState.value = SingleChatState.ShowError(message)


    }
    fun <T> readFromSP(key: String, clazz: Class<T>): T {
        return getFromSharedPreferenceUseCase(key = key, clazz = clazz)
    }

    fun setInMyChats(key: String, value: Boolean) {
        saveInSharedPreferenceUseCase(key, value)
    }
}