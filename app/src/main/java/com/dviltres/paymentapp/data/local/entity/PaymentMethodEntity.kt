package com.dviltres.paymentapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PaymentMethodEntity(
    val name: String,
    val secure_thumbnail: String,
    val thumbnail: String,
    @PrimaryKey(autoGenerate = false) val id:String
)