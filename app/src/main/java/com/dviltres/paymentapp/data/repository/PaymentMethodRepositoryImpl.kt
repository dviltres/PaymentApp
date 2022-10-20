package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.data.local.PaymentMethodDao
import com.dviltres.paymentapp.data.mapper.toPaymentMethod
import com.dviltres.paymentapp.data.mapper.toPaymentMethodEntity
import com.dviltres.paymentapp.data.remote.PaymentMarketApi
import com.dviltres.paymentapp.data.remote.dto.paymentMethod.GetPaymentMethodsDto
import com.dviltres.paymentapp.di.ApiKey
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.repository.PaymentMethodRepository

class PaymentMethodRepositoryImpl(
    private val dao: PaymentMethodDao,
    private val api: PaymentMarketApi,
    @ApiKey private val apiKey: String,
): PaymentMethodRepository {

    override suspend fun getPaymentMethods(query:String,  page:Int): List<PaymentMethod> {
        syncPaymentMethods()
        return dao.getPaymentMethods(query = query, page = page, pageSize = Constants.PAGE_SIZE).map {
            it.toPaymentMethod()
        }
    }

    override suspend fun getPaymentMethodsById(paymentMethodId: String): PaymentMethod? {
        return dao.getPaymentMethodById(paymentMethodId)?.toPaymentMethod()
    }

    private suspend fun syncPaymentMethods() {
        val response = try {
            api.getPaymentMethods(apiKey)
        } catch (e: Exception) {
            null
        }
        if(response != null && response.isSuccessful) {
            response?.body()?.let {
                 dao.deletePaymentMethods()
                 insertPaymentMethods(it)
            }
        }
    }

    private suspend fun insertPaymentMethods(items: GetPaymentMethodsDto) {
        dao.insertPaymentMethods(items.map {
            it.toPaymentMethodEntity()
        })
    }
}