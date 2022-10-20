package com.dviltres.paymentapp.presentation.installment

import androidx.compose.ui.geometry.Size
import com.dviltres.paymentapp.domain.model.*
import com.dviltres.paymentapp.presentation.components.SearchState

data class InstallmentState(
    val isLoading:Boolean = false,
    val installment:Installment? = null,
    val recommendedMessage:List<String> = emptyList(),
    val error: String = "",
    val isRefreshing: Boolean = false,
    val searchState: SearchState = SearchState.CLOSED,
    val selectedInstallment: String = "",
    val amount:String = "",
    val paymentMethodId:String? = null,
    val issuerId:String? = null,
    val token:String="",
    val creditCard: CreditCard = CreditCard(),
    val payment:Payment = Payment(),
    val selectedInstallmentMessageSize: Size = Size.Zero,
    val installmentExpanded:Boolean = false,
)
