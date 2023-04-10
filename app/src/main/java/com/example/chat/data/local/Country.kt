package com.example.chat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_table")
data class Country(
@PrimaryKey(autoGenerate = true)
val id :Int,
val code: String,
val name: String,
val noCode: String,
val money: String
)

