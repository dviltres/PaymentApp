package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.data.local.InstallmentDao
import com.dviltres.paymentapp.data.mapper.toInstallment
import com.dviltres.paymentapp.data.mapper.toInstallmentEntity
import com.dviltres.paymentapp.data.remote.PaymentMarketApi
import com.dviltres.paymentapp.data.remote.dto.installment.GetInstallmentsDto
import com.dviltres.paymentapp.di.ApiKey
import com.dviltres.paymentapp.domain.model.Installment
import com.dviltres.paymentapp.domain.repository.InstallmentRepository

class InstallmentRepositoryImpl(
    private val dao: InstallmentDao,
    private val api: PaymentMarketApi,
    @ApiKey private val apiKey: String,
): InstallmentRepository {
    override suspend fun getInstallment(
        amount: Double,
        paymentMethodId: String,
        issuerId: String
    ): Installment? {
        syncInstallment(amount = amount, paymentMethodId = paymentMethodId, issuerId = issuerId)
        return dao.getInstallments(issuerId = issuerId, paymentMethodId = paymentMethodId)?.let {
           return it.toInstallment()
        }
    }

    private suspend fun syncInstallment(amount:Double, paymentMethodId:String, issuerId: String) {
        val response = try {
            api.getInstallments(
                publicKey = apiKey,
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