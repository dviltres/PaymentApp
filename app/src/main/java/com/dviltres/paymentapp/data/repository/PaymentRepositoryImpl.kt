package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.data.local.CardIssuerDao
import com.dviltres.paymentapp.data.local.PaymentDao
import com.dviltres.paymentapp.data.local.PaymentMethodDao
import com.dviltres.paymentapp.data.mapper.toCardIssuer
import com.dviltres.paymentapp.data.mapper.toPayment
import com.dviltres.paymentapp.data.mapper.toPaymentEntity
import com.dviltres.paymentapp.data.mapper.toPaymentMethod
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.model.CreditCard
import com.dviltres.paymentapp.domain.model.Payment
import com.dviltres.paymentapp.domain.model.PaymentMethod
import com.dviltres.paymentapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val paymentDao: PaymentDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val cardIssuerDao: CardIssuerDao
): PaymentRepository {
    override suspend fun getPayments(): List<Payment> {
        var result = mutableListOf<Payment>()

         paymentDao.getPaymentsByDate().onEach { payment ->
             var paymentMethod = getPaymentMethodById(payment.paymentMethodId)
             var cardIssuer = getCardIssuerById(payment.cardIssuerId)

             if(paymentMethod != null && cardIssuer != null)
                 result.add(payment.toPayment(paymentMethod = paymentMethod!!, cardIssuer = cardIssuer!!))
         }
        return result
    }

    override suspend fun paymentConfirm(card: CreditCard, payment: Payment): Payment? {
        //* Check out the card info to complete the payment *//
        return try {
            var paymentMethod = getPaymentMethodById(payment.paymentMethod!!.id)
            var cardIssuer = getCardIssuerById(payment.cardIssuer!!.id)
            paymentDao.getPaymentById(paymentDao.insertPayment(payment.toPaymentEntity()))!!.toPayment(paymentMethod = paymentMethod!!, cardIssuer = cardIssuer!!)
        } catch (e:Exception){
            null
        }
    }

    private suspend fun getPaymentMethodById(paymentMethodId: String):PaymentMethod? {
        return paymentMethodDao.getPaymentMethodById(paymentMethodId)?.let { paymentMethodEntity->
             paymentMethodEntity.toPaymentMethod()
        }
    }

    private suspend fun getCardIssuerById(cardIssuerId: String):CardIssuer? {
        return cardIssuerDao.getCardIssuerById(cardIssuerId)?.let { cardIssuerEntity->
            cardIssuerEntity.toCardIssuer()
        }
    }




}