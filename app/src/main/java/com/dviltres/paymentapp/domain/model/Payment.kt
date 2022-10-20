package com.dviltres.paymentapp.domain.model

import java.util.*


data class Payment(
    val paymentMethod:PaymentMethod? = null,
    val cardIssuer:CardIssuer? = null,
    val amount:Double? = null,
    val installment: String? = null,
    val date: Date? = null
)
