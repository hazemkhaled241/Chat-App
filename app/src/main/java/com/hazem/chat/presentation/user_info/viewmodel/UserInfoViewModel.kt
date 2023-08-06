package com.hazem.chat.presentation.user_info.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.domain.usecase.remote.auth.SaveUserDateUseCase
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val saveUserDateUseCase: SaveUserDateUseCase
):ViewModel(){
    private var _saveUserDataState = MutableStateFlow<SaveUserDataState>(SaveUserDataState.Init)
    val saveUserNameState = _saveUserDataState.asStateFlow()
    fun saveUserName(userName:String,uri: Uri= Uri.parse("")){
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            saveUserDateUseCase.invoke(userName, uri).let {
                when (it) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _saveUserDataState.value = SaveUserDataState.ShowError(it.message)
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            setLoading(false)
                            _saveUserDataState.value = SaveUserDataState.SavedSuccessfully(it.data)
                        }
                    }
                }
            }

        }
    }



    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _saveUserDataState.value = SaveUserDataState.IsLoading(true)
            }
            false -> {
                _saveUserDataState.value = SaveUserDataState.IsLoading(false)
            }
        }
    }

}