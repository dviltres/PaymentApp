package com.dviltres.paymentapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dviltres.paymentapp.data.local.entity.CardIssuerEntity
import com.dviltres.paymentapp.data.local.entity.InstallmentEntity
import com.dviltres.paymentapp.data.local.entity.PaymentEntity
import com.dviltres.paymentapp.data.local.entity.PaymentMethodEntity

@Database(
    entities = [ PaymentEntity::class, PaymentMethodEntity::class, InstallmentEntity::class, CardIssuerEntity::class],
    version = 1
)
abstract class PaymentAppDatabase: RoomDatabase() {
    abstract val paymentDao: PaymentDao
    abstract val paymentMethodDao: PaymentMethodDao
    abstract val installmentDao:InstallmentDao
    abstract val cardIssuerDao:CardIssuerDao
}