package com.dviltres.paymentapp.presentation.paymentMethod

import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.presentation.components.SearchState

data class PaymentMethodState(
    val query: String = "",
    val page:Int = 0,
    val isLoading:Boolean = false,
    val paymentMethods:List<PaymentMethod> = emptyList(),
    val expanded:Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false,
    val searchState: SearchState = SearchState.CLOSED,
    val selectedPaymentMethod: PaymentMethod? = null,
)
