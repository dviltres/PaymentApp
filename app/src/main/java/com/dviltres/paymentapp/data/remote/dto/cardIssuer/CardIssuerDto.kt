package com.dviltres.paymentapp.data.remote.dto.cardIssuer

data class CardIssuerDto(
    val id: String,
    val merchant_account_id: Any,
    val name: String,
    val processing_mode: String,
    val secure_thumbnail: String,
    val status: String,
    val thumbnail: String
)