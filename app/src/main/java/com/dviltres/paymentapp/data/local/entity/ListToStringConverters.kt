package com.dviltres.paymentapp.data.local.entity

import androidx.room.TypeConverter
import com.google.gson.Gson

object ListToStringConverters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}