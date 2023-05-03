package com.hazem.chat.presentation.login.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.data.local.Country
import com.hazem.chat.domain.usecase.remote.auth.IsValidNumberUseCase
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val isValidNumberUseCase: IsValidNumberUseCase
) : ViewModel() {

    private var _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val loginState = _loginState.asStateFlow()
    //private val _loginState = MutableLiveData<LoginState>()
    //val loginState: LiveData<LoginState> get() = _loginState
    fun isValidNumber(country: Country, phoneNumber: String, activity: Activity) {
        setLoading(true)


        viewModelScope.launch(Dispatchers.IO) {
                isValidNumberUseCase.invoke(country, phoneNumber, activity).collect {
                    when (it) {
                        is Resource.Error -> {
                            withContext(Dispatchers.Main) {
                                setLoading(false)
                                delay(500)
                                _loginState.value = LoginState.ShowError(it.message)
                            }


                        }
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                setLoading(false)
                                delay(500)
                                    _loginState.value = LoginState.ValidNumber(it.data)
                            }
                        }
                    }
                }


        }
    }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _loginState.value = LoginState.IsLoading(true)
            }
            false -> {
                _loginState.value = LoginState.IsLoading(false)
            }
        }
    }
}