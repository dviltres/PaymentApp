package com.dviltres.paymentapp.di

import android.content.Context
import androidx.room.Room
import com.dviltres.paymentapp.data.common.Constants
import com.dviltres.paymentapp.data.local.PaymentAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, PaymentAppDatabase::class.java, Constants.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providePaymentDao(db: PaymentAppDatabase) = db.paymentDao

    @Singleton
    @Provides
    fun provideCardIssuerDao(db: PaymentAppDatabase) = db.cardIssuerDao

    @Singleton
    @Provides
    fun provideInstallmentDao(db: PaymentAppDatabase) = db.installmentDao


    @Singleton
    @Provides
    fun providePaymentMethodDao(db: PaymentAppDatabase) = db.paymentMethodDao
}