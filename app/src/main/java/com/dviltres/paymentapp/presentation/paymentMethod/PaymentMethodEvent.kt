package com.dviltres.paymentapp.presentation.paymentMethod

import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.presentation.components.SearchState

sealed class PaymentMethodEvent {
    object OnConfirmClick: PaymentMethodEvent()
    object OnRefresh: PaymentMethodEvent()
    data class OnUpdateSearchText(val query: String): PaymentMethodEvent()
    data class OnUpdateSearchState(val searchState: SearchState): PaymentMethodEvent()
    data class OnChangeScrollPosition(val position: Int): PaymentMethodEvent()
    object OnTriggerNextPage:PaymentMethodEvent()
    data class OnSelectPaymentMethod(val paymentMethod: PaymentMethod): PaymentMethodEvent()
    data class OnShowErrorMessage(val error: String): PaymentMethodEvent()
}
