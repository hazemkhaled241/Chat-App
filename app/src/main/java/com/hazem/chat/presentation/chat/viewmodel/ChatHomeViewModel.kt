package com.hazem.chat.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.domain.model.Contact
import com.hazem.chat.domain.usecase.remote.chat.GetRegisteredContactUseCase
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
class ChatHomeViewModel @Inject constructor(private val getRegisteredContactUseCase: GetRegisteredContactUseCase,
private val saveInSharedPreferenceUseCase: SaveInSharedPreferenceUseCase) :
    ViewModel() {
    private var _chatHomeState = MutableStateFlow<ChatHomeState>(ChatHomeState.Init)
    val chatHomeState = _chatHomeState.asStateFlow()
    fun fetchRegisteredContacts(
        contacts: ArrayList<Contact>
    ) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            getRegisteredContactUseCase(contacts).let {
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
                            _chatHomeState.value =
                                ChatHomeState.FetchAllRegisteredContactsSuccessfullySuccessfully(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _chatHomeState.value = ChatHomeState.IsLoading(true)
            }
            false -> {
                _chatHomeState.value = ChatHomeState.IsLoading(false)
            }
        }
    }

    private fun showError(message: String) {

        _chatHomeState.value = ChatHomeState.ShowError(message)


    }

    fun setInMyChats(key: String, value: Boolean) {
        saveInSharedPreferenceUseCase(key, value)
    }

}