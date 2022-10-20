package com.dviltres.paymentapp.data.remote

import com.dviltres.paymentapp.data.remote.dto.cardIssuer.GetCardIssuersDto
import com.dviltres.paymentapp.data.remote.dto.installment.GetInstallmentsDto
import com.dviltres.paymentapp.data.remote.dto.paymentMethod.GetPaymentMethodsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentMarketApi {

    @GET("/v1/payment_methods")
    suspend fun getPaymentMethods(@Query("public_key") publicKey: String): Response<GetPaymentMethodsDto>

    @GET("/v1/payment_methods/card_issuers")
    suspend fun getCardIssuers(
        @Query("public_key") publicKey: String,
        @Query("payment_method_id") paymentMethodId: String,
    ): Response<GetCardIssuersDto>

    @GET("/v1/payment_methods/installments")
    suspend fun getInstallments(
        @Query("public_key") publicKey: String,
        @Query("amount") amount: Double,
        @Query("payment_method_id") paymentMethodId: String,
        @Query("issuer.id")  issuerId: String,
    ): Response<GetInstallmentsDto>

    companion object {
        const val BASE_URL = "https://api.mercadopago.com"
    }

}