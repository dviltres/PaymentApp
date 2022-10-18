package com.dviltres.paymentapp.data.remote.dto.installment

data class PayerCostDto(
    val discount_rate: Int,
    val installment_amount: Double,
    val installment_rate: Double,
    val installment_rate_collector: List<String>,
    val installments: Int,
    val labels: List<String>,
    val max_allowed_amount: Int,
    val min_allowed_amount: Int,
    val payment_method_option_id: String,
    val recommended_message: String,
    val reimbursement_rate: Any,
    val total_amount: Double
)