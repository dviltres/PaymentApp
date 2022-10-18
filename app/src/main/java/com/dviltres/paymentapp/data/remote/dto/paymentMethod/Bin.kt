package com.dviltres.paymentapp.data.remote.dto.paymentMethod

data class Bin(
    val exclusion_pattern: String,
    val installments_pattern: String,
    val pattern: String
)