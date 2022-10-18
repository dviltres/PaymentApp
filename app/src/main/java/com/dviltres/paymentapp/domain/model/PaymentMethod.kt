package com.dviltres.paymentapp.domain.model

data class PaymentMethod(
    val id: String,
    val name: String,
    val secure_thumbnail: String,
    val thumbnail: String
)
