package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.BuildConfig
import com.dviltres.paymentapp.data.local.InstallmentDao
import com.dviltres.paymentapp.data.mapper.toInstallment
import com.dviltres.paymentapp.data.mapper.toInstallmentEntity
import com.dviltres.paymentapp.data.remote.PaymentMarketApi
import com.dviltres.paymentapp.data.remote.dto.installment.GetInstallmentsDto
import com.dviltres.paymentapp.domain.model.Installment
import com.dviltres.paymentapp.domain.repository.InstallmentRepository

class InstallmentRepositoryImpl(
    private val dao: InstallmentDao,
    private val api: PaymentMarketApi
): InstallmentRepository {
    override suspend fun getInstallment(
        amount: Int,
        paymentMethodId: String,
        issuerId: String
    ): Installment? {
        val isDbEmpty = dao.getCantInstallment() <= 0
        if (isDbEmpty) {
            syncInstallment(amount = amount, paymentMethodId = paymentMethodId, issuerId = issuerId)
        }
       // syncInstallment(amount = amount, paymentMethodId = paymentMethodId, issuerId = issuerId)
        return dao.getInstallments(paymentMethodId)?.let {
           return it.toInstallment()
        }
    }
    private suspend fun syncInstallment(amount:Int, paymentMethodId:String, issuerId: String) {
        val response = try {
            api.getInstallments(
                publicKey = BuildConfig.PUBLIC_KEY,
                amount = amount,
                paymentMethodId = paymentMethodId,
                issuerId = issuerId
            )
        } catch (e: Exception) {
            null
        }
        if(response != null && response.isSuccessful) {
            response?.body()?.let {
                dao.deleteInstallments()
                insertInstallments(it)
            }
        }
    }

    private suspend fun insertInstallments(items: GetInstallmentsDto) {
        dao.insertInstallments(items.map {
            it.toInstallmentEntity()
        })
    }

}