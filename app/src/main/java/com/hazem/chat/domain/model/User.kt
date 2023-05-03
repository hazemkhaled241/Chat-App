package com.hazem.chat.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId:String="",
    val phoneNumber:String="",
    val name:String=""
):Parcelable
