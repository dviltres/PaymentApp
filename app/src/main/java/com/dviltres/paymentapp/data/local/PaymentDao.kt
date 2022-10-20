package com.dviltres.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dviltres.paymentapp.data.local.entity.InstallmentEntity
import com.dviltres.paymentapp.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(paymentEntity: PaymentEntity):Long

    @Query(" SELECT * FROM PaymentEntity ORDER BY date DESC ")
    suspend fun getPaymentsByDate(): List<PaymentEntity>

    @Query("SELECT * FROM PaymentEntity Where id=:paymentId")
    suspend fun getPaymentById(paymentId: Long): PaymentEntity?

}