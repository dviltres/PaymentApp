package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.Payment

interface PaymentRepository {
    suspend fun getPayments(query:String): List<Payment>
}