package com.example.chat.domain.usecase
import com.example.chat.utils.Resource
import com.google.i18n.phonenumbers.PhoneNumberUtil
import javax.inject.Inject

class IsValidNumberUseCase @Inject constructor(
    private val phoneNumberUtil: PhoneNumberUtil
) {
    operator fun invoke(code: String, phoneNumber: String): Resource<String, String> {
        try {
            return if (code.isEmpty())
                Resource.Error("Please select a country ")
            else if (phoneNumber.isEmpty())
                Resource.Error("Please enter valid mobile number ")
            else if (!phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(phoneNumber, code))){
                Resource.Error("Please enter valid mobile number ")}
            else {
                Resource.Success("Code send successfully")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
       return Resource.Error("Please enter valid mobile number ")
    }
}