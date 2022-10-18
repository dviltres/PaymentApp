package com.dviltres.paymentapp.domain.repository

import com.dviltres.paymentapp.domain.model.CardIssuer

interface CardIssuerRepository {
    suspend fun getCardIssuers(query:String, paymentMethodId:String): List<CardIssuer>
    suspend fun getCardIssuerId(cardIssuerId:String): CardIssuer?
}