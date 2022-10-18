package com.dviltres.paymentapp.data.remote.dto.paymentMethod

data class PaymentMethodDto(
    val accreditation_time: Int,
    val additional_info_needed: List<String>,
    val deferred_capture: String,
    val financial_institutions: List<Any>,
    val id: String,
    val max_allowed_amount: Int,
    val min_allowed_amount: Int,
    val name: String,
    val payment_type_id: String,
    val processing_modes: List<String>,
    val secure_thumbnail: String,
    val settings: List<Setting>,
    val status: String,
    val thumbnail: String
)