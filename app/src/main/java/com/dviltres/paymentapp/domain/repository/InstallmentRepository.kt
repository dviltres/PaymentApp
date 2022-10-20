package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.Installment

interface InstallmentRepository {
    suspend fun getInstallment(amount:Double, paymentMethodId:String, issuerId:String): Installment?
}