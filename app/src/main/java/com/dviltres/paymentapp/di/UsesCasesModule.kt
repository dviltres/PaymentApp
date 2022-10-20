package com.dviltres.paymentapp.di

import android.app.Application
import com.dviltres.paymentapp.domain.repository.CardIssuerRepository
import com.dviltres.paymentapp.domain.repository.InstallmentRepository
import com.dviltres.paymentapp.domain.repository.PaymentMethodRepository
import com.dviltres.paymentapp.domain.repository.PaymentRepository
import com.dviltres.paymentapp.domain.useCase.cardIssuer.CardIssuerUseCases
import com.dviltres.paymentapp.domain.useCase.cardIssuer.GetCardIssuerById
import com.dviltres.paymentapp.domain.useCase.cardIssuer.GetCardIssuers
import com.dviltres.paymentapp.domain.useCase.installment.GetInstallment
import com.dviltres.paymentapp.domain.useCase.installment.InstallmentUseCases
import com.dviltres.paymentapp.domain.useCase.payment.GetPayments
import com.dviltres.paymentapp.domain.useCase.payment.PaymentConfirm
import com.dviltres.paymentapp.domain.useCase.payment.PaymentUseCases
import com.dviltres.paymentapp.domain.useCase.paymentMethod.GetPaymentMethodById
import com.dviltres.paymentapp.domain.useCase.paymentMethod.GetPaymentMethods
import com.dviltres.paymentapp.domain.useCase.paymentMethod.PaymentMethodUseCases
import com.dviltres.paymentapp.domain.useCase.util.FilterOutDigits
import com.dviltres.paymentapp.domain.useCase.util.InputValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsesCasesModule {

    /* ---------------------PaymentUseCases---------------------------------*/
    @Provides
    @Singleton
    fun providePaymentUseCases(repository:PaymentRepository, application: Application): PaymentUseCases {
        return PaymentUseCases(
            getPayments = GetPayments(repository = repository, application = application),
            paymentConfirm = PaymentConfirm(repository = repository, application = application)
        )
    }

    /* ---------------------PaymentMethodUseCases---------------------------------*/
    @Provides
    @Singleton
    fun providePaymentMethodUseCases(
        repository: PaymentMethodRepository,
        application: Application
    ): PaymentMethodUseCases {
        return PaymentMethodUseCases(
            getPaymentMethods = GetPaymentMethods(repository = repository, application = application),
            getPaymentMethodById = GetPaymentMethodById(repository = repository, application = application)
        )
    }

    /* ---------------------CardIssuerUseCases---------------------------------*/
    @Provides
    @Singleton
    fun provideCardIssuerUseCases(repository: CardIssuerRepository, application: Application): CardIssuerUseCases {
        return CardIssuerUseCases(
            getCardIssuers = GetCardIssuers(repository = repository, application = application),
            getCardIssuerById = GetCardIssuerById(repository = repository, application = application)
        )
    }

    /* ---------------------InstallmentUseCases---------------------------------*/
    @Provides
    @Singleton
    fun provideInstallmentUseCases(repository: InstallmentRepository, application: Application): InstallmentUseCases {
        return InstallmentUseCases(
            getInstallment = GetInstallment(repository = repository, application = application)
        )
    }




}