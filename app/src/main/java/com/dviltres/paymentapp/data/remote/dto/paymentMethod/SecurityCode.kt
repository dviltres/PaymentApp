package com.dviltres.paymentapp.data.remote.dto.paymentMethod

data class SecurityCode(
    val card_location: String,
    val length: Int,
    val mode: String
)