package com.dviltres.paymentapp.domain.model


data class Payment(
    val paymentMethod:PaymentMethod,
    val cardIssuer:CardIssuer,
    val amount:Double,
    val installment: String,
)
