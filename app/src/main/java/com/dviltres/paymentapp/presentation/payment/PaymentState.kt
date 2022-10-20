package com.dviltres.paymentapp.presentation.payment

import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.presentation.components.SearchState

data class PaymentState(
    val isLoading:Boolean = false,
    val amount:String = "",
    val payments:List<Payment> = emptyList(),
    val expanded:Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false
)
