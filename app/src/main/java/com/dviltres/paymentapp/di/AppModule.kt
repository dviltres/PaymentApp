package com.dviltres.paymentapp.di

import android.app.Application
import android.content.Context
import com.dviltres.paymentapp.PaymentApp
import com.dviltres.paymentapp.domain.useCase.payment.PaymentUseCases
import com.dviltres.paymentapp.presentation.payment.PaymentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): PaymentApp {
        return app as PaymentApp
    }
}