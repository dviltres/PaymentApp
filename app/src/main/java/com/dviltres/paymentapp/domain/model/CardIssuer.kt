package com.dviltres.paymentapp.domain.model

data class CardIssuer(
    val id: String,
    val name: String,
    val secure_thumbnail: String,
    val thumbnail: String
)
