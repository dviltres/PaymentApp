package com.dviltres.paymentapp.presentation.installment

import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.domain.model.Installment
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.presentation.components.SearchState

data class InstallmentState(
    val isLoading:Boolean = false,
    val installment:Installment? = null,
    val recommendedMessage:List<String> = emptyList(),
    val error: String = "",
    val isRefreshing: Boolean = false,
    val searchState: SearchState = SearchState.CLOSED,
    val selectedInstallment: String = "",
    val paymentMethod: PaymentMethod? = null,
    val cardIssuer:CardIssuer? = null,
    val amount:Int? = null,
    val paymentMethodId:String? = null,
    val issuerId:String? = null,
    val token:String="",
    val creditCard: CreditCard = CreditCard(),
    val flipped:Boolean = false
)
