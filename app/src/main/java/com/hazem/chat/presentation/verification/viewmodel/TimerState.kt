package com.hazem.chat.presentation.verification.viewmodel

sealed class TimerState{
    data class Finished(val isFinished: Boolean) : TimerState()

}
