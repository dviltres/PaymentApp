package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.data.local.CardIssuerDao
import com.dviltres.paymentapp.data.local.PaymentDao
import com.dviltres.paymentapp.data.local.PaymentMethodDao
import com.dviltres.paymentapp.data.mapper.toCardIssuer
import com.dviltres.paymentapp.data.mapper.toPayment
import com.dviltres.paymentapp.data.mapper.toPaymentMethod
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import com.dviltres.paymentapp.domain.useCase.util.getCurrentDateTime
import com.dviltres.paymentapp.domain.useCase.util.toString

class PaymentRepositoryImpl(
    private val paymentDao: PaymentDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val cardIssuerDao: CardIssuerDao
): PaymentRepository {
    override suspend fun getPayments(query:String): List<Payment> {
        val date = getCurrentDateTime().toString(Constants.DATE_FORMAT)
        return paymentDao.getPaymentsByDate(query = query, date = date).mapNotNull { payment ->
            var paymentMethod: PaymentMethod? = null
            var cardIssuer: CardIssuer? = null

             paymentMethodDao.getPaymentMethodById(payment.paymentMethodId)?.let { paymentMethodEntity->
                 paymentMethod = paymentMethodEntity.toPaymentMethod()
             }
             cardIssuerDao.getCardIssuerById(payment.cardIssuerId)?.let { cardIssuerEntity->
                 cardIssuer = cardIssuerEntity.toCardIssuer()
             }

            if(paymentMethod != null && cardIssuer != null)
                payment.toPayment(paymentMethod = paymentMethod!!, cardIssuer = cardIssuer!!)
            else
                null
        }
    }

}