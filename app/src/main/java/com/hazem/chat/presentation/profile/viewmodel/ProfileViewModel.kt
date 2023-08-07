package com.hazem.chat.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.usecase.remote.chat.GetUserByIdUseCase
import com.hazem.chat.domain.usecase.remote.shared_preference.GetFromSharedPreferenceUseCase
import com.hazem.chat.domain.usecase.update_profile.UpdateProfileUseCase
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getFromSharedPreferenceUseCase: GetFromSharedPreferenceUseCase
) : ViewModel() {
    private var _updateProfileState = MutableSharedFlow<UpdateProfileState>()
    val updateProfileState = _updateProfileState.asSharedFlow()
    private var _getUserState = MutableStateFlow<GetUserByIdState>(GetUserByIdState.Init)
    val getUserState = _getUserState.asStateFlow()
    fun updateProfile(userId: String, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            updateProfileUseCase.invoke(userId, user).let {

                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        _updateProfileState.emit( UpdateProfileState.ShowError(it.message))
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        _updateProfileState.emit( UpdateProfileState.UpdatedSuccessfully(it.data))
                    }
                }
            }
        }
    }
    fun <T> getFromSP(key: String, clazz: Class<T>): T {
        return getFromSharedPreferenceUseCase(key, clazz)
    }
    fun fetchCurrentUserById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase(id).let {
                when (it) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _getUserState.value =
                                GetUserByIdState.ShowError(it.message)
                        }
                    }

                    is Resource.Success -> {
                        _getUserState.value =
                            GetUserByIdState.GetUserByIdSuccessfully(it.data)
                    }
                }
            }
        }
    }
    private suspend fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _updateProfileState.emit( UpdateProfileState.IsLoading(true))
            }
            false -> {
                _updateProfileState.emit( UpdateProfileState.IsLoading(false))
            }
        }
    }
}