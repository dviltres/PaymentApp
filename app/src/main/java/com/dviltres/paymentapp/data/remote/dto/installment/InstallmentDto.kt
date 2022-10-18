package com.dviltres.paymentapp.data.remote.dto.installment

data class InstallmentDto(
    val agreements: Any,
    val issuer: Issuer,
    val merchant_account_id: Any,
    val payer_costs: List<PayerCostDto>,
    val payment_method_id: String,
    val payment_type_id: String,
    val processing_mode: String
)