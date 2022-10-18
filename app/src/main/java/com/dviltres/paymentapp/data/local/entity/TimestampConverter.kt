package com.dviltres.paymentapp.data.local.entity

import androidx.room.TypeConverter
import com.dviltres.paymentapp.data.local.entity.PaymentEntity.Companion.format
import java.util.*

class TimestampConverter {
    @TypeConverter
    fun fromDateToString(value: Date): String {
        return format.format(value)
    }
    @TypeConverter
    fun toDateFromString(value: String) : Date {
        return format.parse(value)
    }
}