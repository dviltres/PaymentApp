package com.dviltres.paymentapp.presentation.cardIssuer

import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.presentation.components.SearchState

sealed class CardIssuerEvent {
    object OnConfirmClick: CardIssuerEvent()
    object OnRefresh: CardIssuerEvent()
    data class OnUpdateSearchText(val query: String): CardIssuerEvent()
    data class OnUpdateSearchState(val searchState: SearchState): CardIssuerEvent()
    data class OnChangeScrollPosition(val position: Int): CardIssuerEvent()
    object OnTriggerNextPage:CardIssuerEvent()
    data class OnSelectPaymentMethod(val cardIssuer: CardIssuer): CardIssuerEvent()
    data class OnShowErrorMessage(val error: String): CardIssuerEvent()
}
