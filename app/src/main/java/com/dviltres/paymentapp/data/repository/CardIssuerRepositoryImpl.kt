package com.dviltres.paymentapp.data.repository

import com.dviltres.paymentapp.data.local.CardIssuerDao
import com.dviltres.paymentapp.data.mapper.toCardIssuer
import com.dviltres.paymentapp.data.mapper.toCardIssuerEntity
import com.dviltres.paymentapp.data.remote.PaymentMarketApi
import com.dviltres.paymentapp.data.remote.dto.cardIssuer.GetCardIssuersDto
import com.dviltres.paymentapp.di.ApiKey
import com.dviltres.paymentapp.domain.model.CardIssuer
import com.dviltres.paymentapp.domain.repository.CardIssuerRepository

class CardIssuerRepositoryImpl(
    private val dao: CardIssuerDao,
    private val api: PaymentMarketApi,
    @ApiKey private val apiKey: String,
): CardIssuerRepository {

    override suspend fun getCardIssuers(query:String, paymentMethodId:String): List<CardIssuer> {
        syncCardIssuers(paymentMethodId)
        return dao.getCardIssuers().map {
            it.toCardIssuer()
        }
    }

    override suspend fun getCardIssuerId(cardIssuerId: String): CardIssuer? {
        return dao.getCardIssuerById(cardIssuerId)?.let {
            it.toCardIssuer()
        }
    }

    private suspend fun syncCardIssuers(paymentMethodId:String) {
        val response = try {
            api.getCardIssuers(paymentMethodId = paymentMethodId, publicKey = apiKey)
        } catch (e: Exception) {
            null
        }
        if(response != null && response.isSuccessful) {
            response?.body()?.let {
                dao.deleteCardIssuers()
                insertCardIssuers(it)
            }
        }
    }

    private suspend fun insertCardIssuers(items: GetCardIssuersDto) {
        dao.insertCardIssuers(items.map {
            it.toCardIssuerEntity()
        })
    }
}