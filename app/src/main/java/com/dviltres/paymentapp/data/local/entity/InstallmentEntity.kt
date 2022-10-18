package com.dviltres.paymentapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(ListToStringConverters::class)
data class InstallmentEntity(
    val payment_method_id: String,
    val recommended_message:List<String>,
    @PrimaryKey(autoGenerate = false) val issuerId: String,
)
