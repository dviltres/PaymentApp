package com.dviltres.paymentapp.presentation.cardIssuer

import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.presentation.components.SearchState

data class CardIssuerState(
    val query: String = "",
    val page:Int = 0,
    val isLoading:Boolean = false,
    val cardIssuers:List<CardIssuer> = emptyList(),
    val expanded:Boolean = false,
    val error: String = "",
    val isRefreshing: Boolean = false,
    val searchState: SearchState = SearchState.CLOSED,
    val selectedCardIssuer: CardIssuer? = null,
    val paymentMethod: PaymentMethod? = null
)
