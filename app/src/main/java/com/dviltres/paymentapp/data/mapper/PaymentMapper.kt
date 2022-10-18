package com.dviltres.paymentapp.data.mapper

import com.dviltres.paymentapp.data.local.entity.PaymentEntity
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.model.PaymentMethod

fun PaymentEntity.toPayment(paymentMethod: PaymentMethod, cardIssuer: CardIssuer): Payment {
    return Payment(
        paymentMethod = paymentMethod,
        cardIssuer = cardIssuer,
        amount = amount,
        installment = installment
    )
}