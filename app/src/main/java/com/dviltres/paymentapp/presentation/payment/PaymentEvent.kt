package com.dviltres.paymentapp.presentation.payment

sealed class PaymentEvent {
    data class OnAmountChange(val amount: String): PaymentEvent()
    object OnConfirmClick: PaymentEvent()
    object OnCloseAddAmountView: PaymentEvent()
    object OnAddAmountClick: PaymentEvent()
    object OnRefresh: PaymentEvent()
}
