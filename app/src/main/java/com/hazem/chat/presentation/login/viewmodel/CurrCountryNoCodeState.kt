package com.hazem.chat.presentation.login.viewmodel

import com.hazem.chat.data.local.Country

sealed class CurrCountryNoCodeState {
    object Init : CurrCountryNoCodeState()
    data class ShowError(val message: String) : CurrCountryNoCodeState()
    data class GetCurrCountryNoCodeSuccessfully(val date: Country) : CurrCountryNoCodeState()
}