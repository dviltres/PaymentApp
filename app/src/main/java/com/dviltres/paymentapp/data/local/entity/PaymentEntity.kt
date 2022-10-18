package com.dviltres.paymentapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dviltres.paymentapp.data.common.Constants
import java.text.SimpleDateFormat
import java.util.*

@Entity
@TypeConverters(TimestampConverter::class)
data class PaymentEntity(
    val amount:Double,
    val paymentMethodId:String,
    val cardIssuerId:String,
    val installment:String,
    val date: Date,
    @PrimaryKey(autoGenerate = true) var id: Long?=null,
){
    companion object {
        val format = SimpleDateFormat(Constants.DATE_FORMAT)
    }
}
