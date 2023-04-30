package com.hazem.chat.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazem.chat.data.local.dao.CountryDao

@Database(entities = [Country::class], version = 1, exportSchema = false)

abstract class CountryDataBase: RoomDatabase() {
    abstract fun countryDao():CountryDao
    companion object{
        @Volatile

        private var INSTANCE: CountryDataBase?=null
        fun getDataBase(context: Context): CountryDataBase {
            val tempInstance= INSTANCE
            if(tempInstance!=null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryDataBase::class.java,
                    "country"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}