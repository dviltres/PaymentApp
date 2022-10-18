package com.dviltres.paymentapp.domain.model

data class Installment(
    val payment_method_id: String,
    val recommended_message: List<String>,
    val issuerId:String,
    val isSelected:Boolean = false,
)
