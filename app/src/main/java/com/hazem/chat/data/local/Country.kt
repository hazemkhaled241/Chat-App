package com.hazem.chat.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "country_table")
 @Parcelize
data class Country(
@PrimaryKey(autoGenerate = true)
val id :Int,
val code: String,
val name: String,
val noCode: String,
val money: String
):Parcelable

