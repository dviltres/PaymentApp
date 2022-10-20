package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.domain.model.Payment

interface PaymentRepository {
    suspend fun getPayments(): List<Payment>
    suspend fun paymentConfirm(card:CreditCard,payment: Payment):Payment?
}