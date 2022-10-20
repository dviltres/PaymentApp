package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.PaymentMethod

interface PaymentMethodRepository {
   suspend fun getPaymentMethods(query:String, page:Int): List<PaymentMethod>
   suspend fun getPaymentMethodsById(paymentMethodId:String): PaymentMethod?
}