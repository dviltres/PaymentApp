package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.Installment

interface InstallmentRepository {
    suspend fun getInstallment(amount:Int, paymentMethodId:String, issuerId:String): Installment?
}