package com.hazem.chat.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.domain.model.User
import com.hazem.chat.domain.usecase.update_profile.UpdateProfileUseCase
import com.hazem.chat.presentation.profile.UpdateProfileState
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private var _updateProfileState = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Init)
    val updateProfileState = _updateProfileState.asStateFlow()
    fun updateProfile(userId: String,user:User) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProfileUseCase(userId,user).let {
                setLoading(true)
                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        _updateProfileState.value = UpdateProfileState.ShowError(it.message)
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        _updateProfileState.value = UpdateProfileState.UpdatedSuccessfully(it.data)
                    }
                }
            }
        }
    }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _updateProfileState.value = UpdateProfileState.IsLoading(true)
            }
            false -> {
                _updateProfileState.value = UpdateProfileState.IsLoading(false)
            }
        }
    }
}