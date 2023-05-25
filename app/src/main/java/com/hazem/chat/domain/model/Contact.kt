package com.hazem.chat.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name:String,
    val number:String,
    val url:Uri?
):Parcelable
