package com.dviltres.paymentapp.data.mapper

import com.dviltres.paymentapp.data.local.entity.PaymentEntity
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.useCase.util.getCurrentDateTime

fun PaymentEntity.toPayment(paymentMethod: PaymentMethod, cardIssuer: CardIssuer): Payment {
    return Payment(
        paymentMethod = paymentMethod,
        cardIssuer = cardIssuer,
        amount = amount,
        installment = installment,
        date = date
    )
}

fun Payment.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        paymentMethodId = paymentMethod!!.id,
        cardIssuerId = cardIssuer!!.id,
        amount = amount!!,
        installment = installment!!,
        date = getCurrentDateTime()
    )
}