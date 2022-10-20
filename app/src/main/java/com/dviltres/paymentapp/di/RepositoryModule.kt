package com.dviltres.paymentapp.di

import com.dviltres.paymentapp.data.local.CardIssuerDao
import com.dviltres.paymentapp.data.local.InstallmentDao
import com.dviltres.paymentapp.data.local.PaymentDao
import com.dviltres.paymentapp.data.local.PaymentMethodDao
import com.dviltres.paymentapp.data.remote.PaymentMarketApi
import com.dviltres.paymentapp.data.repository.CardIssuerRepositoryImpl
import com.dviltres.paymentapp.data.repository.InstallmentRepositoryImpl
import com.dviltres.paymentapp.data.repository.PaymentMethodRepositoryImpl
import com.dviltres.paymentapp.data.repository.PaymentRepositoryImpl
import com.dviltres.paymentapp.domain.repository.CardIssuerRepository
import com.dviltres.paymentapp.domain.repository.InstallmentRepository
import com.dviltres.paymentapp.domain.repository.PaymentMethodRepository
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /* ---------------------PaymentRepository---------------------------------*/
    @Provides
    @Singleton
    fun providePaymentRepository(
        paymentDao: PaymentDao,
        paymentMethodDao: PaymentMethodDao,
        cardIssuerDao: CardIssuerDao
    ): PaymentRepository {
        return PaymentRepositoryImpl(
            paymentDao = paymentDao,
            paymentMethodDao = paymentMethodDao,
            cardIssuerDao = cardIssuerDao
        )
    }
    /* ---------------------PaymentMethodRepository---------------------------------*/
    @Provides
    @Singleton
    fun providePaymentMethodRepository( dao: PaymentMethodDao, api: PaymentMarketApi, @ApiKey apiKey: String): PaymentMethodRepository {
        return PaymentMethodRepositoryImpl(dao = dao, api = api, apiKey = apiKey)
    }
    /* ---------------------InstallmentRepository---------------------------------*/
    @Provides
    @Singleton
    fun provideInstallmentRepository(dao: InstallmentDao, api: PaymentMarketApi, @ApiKey apiKey: String): InstallmentRepository {
        return InstallmentRepositoryImpl(dao = dao, api = api, apiKey = apiKey)
    }

    /* ---------------------CardIssuerRepository---------------------------------*/
    @Provides
    @Singleton
    fun provideCardIssuerRepository(dao: CardIssuerDao, api: PaymentMarketApi, @ApiKey apiKey: String): CardIssuerRepository {
        return CardIssuerRepositoryImpl(dao = dao, api = api, apiKey = apiKey)
    }
}