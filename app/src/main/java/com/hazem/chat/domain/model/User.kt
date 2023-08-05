package com.hazem.chat.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId:String,
    val phoneNumber:String,
    val name:String,
    val uri:Uri
):Parcelable
