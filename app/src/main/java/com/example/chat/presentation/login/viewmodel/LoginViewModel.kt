package com.example.chat.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.domain.usecase.IsValidNumberUseCase
import com.example.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val isValidNumberUseCase: IsValidNumberUseCase
):ViewModel() {

    private var _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    val loginState = _loginState.asStateFlow()
  fun isValidNumber(code:String,phoneNumber:String){
      setLoading(true)

      viewModelScope.launch (Dispatchers.IO){
          delay(1000)
          isValidNumberUseCase.invoke(code,phoneNumber).let {
              when(it){
                  is Resource.Error -> {
                      setLoading(false)
                      delay(500)
                      _loginState.value=LoginState.ShowError(it.message)

                  }
                  is Resource.Success -> {
                      setLoading(false)
                      delay(500)
                      _loginState.value=LoginState.ValidNumber(it.data)

                  }
              }
          }
      }
  }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> { _loginState.value = LoginState.IsLoading(true) }
            false -> { _loginState.value = LoginState.IsLoading(false) }
        }
    }
}