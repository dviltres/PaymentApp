package com.dviltres.paymentapp.presentation.payment

import com.dviltres.paymentapp.presentation.components.SearchState

sealed class PaymentEvent {
    data class OnPaymentChange(val amount: String): PaymentEvent()
    object OnConfirmClick: PaymentEvent()
    object OnCloseAddPaymentView: PaymentEvent()
    object OnAddPaymentClick: PaymentEvent()
    object OnRefresh: PaymentEvent()
    data class OnUpdateSearchText(val query: String): PaymentEvent()
    data class OnUpdateSearchState(val searchState: SearchState): PaymentEvent()
    data class OnChangeScrollPosition(val position: Int): PaymentEvent()
    object OnTriggerNextPage:PaymentEvent()
}
