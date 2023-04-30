package com.hazem.chat.presentation.verification.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazem.chat.domain.usecase.VerificationUseCase
import com.hazem.chat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val verificationUseCase: VerificationUseCase
) : ViewModel() {
    private var timer: CountDownTimer? = null
    private var _loginState = MutableStateFlow<VerificationState>(VerificationState.Init)
    val loginState = _loginState.asStateFlow()


    private val _countDownTime = MutableLiveData<String?>()
    val countDownTime: LiveData<String?> get() = _countDownTime

    fun verifyOtp(otpCode: String, verificationID: String) {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            verificationUseCase.invoke(otpCode, verificationID).let {
                when (it) {
                    is Resource.Error -> {
                        setLoading(false)
                        delay(500)
                        _loginState.value = VerificationState.ShowError(it.message)
                    }
                    is Resource.Success -> {
                        setLoading(false)
                            _loginState.value = VerificationState.LoginSuccessfully(it.data)

                    }
                }
            }


        }
    }

    private fun setLoading(status: Boolean) {
        when (status) {
            true -> {
                _loginState.value = VerificationState.IsLoading(true)
            }
            false -> {
                _loginState.value = VerificationState.IsLoading(false)
            }
        }
    }

    fun startCountDown() {
        val startTime = 60
        timer?.cancel()
        timer = object : CountDownTimer(startTime * 1000.toLong(), 1000) {
            override fun onTick(p0: Long) {
                _countDownTime.value = "00:"+(p0 / 1000).toInt().toString()
            }

            override fun onFinish() {
                _countDownTime.value = "Resend Code"
            }

        }
        timer?.start()

    }


}