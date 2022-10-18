package com.dviltres.paymentapp.domain.useCase.paymentMethod

data class PaymentMethodUseCases(
    val getPaymentMethods : GetPaymentMethods,
    val getPaymentMethodById: GetPaymentMethodById
)
