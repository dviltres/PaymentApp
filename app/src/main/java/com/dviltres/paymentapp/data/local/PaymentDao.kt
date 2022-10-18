package com.dviltres.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dviltres.paymentapp.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(paymentEntity: PaymentEntity)

    @Query(""" 
            SELECT * FROM PaymentEntity WHERE (paymentMethodId LIKE '%' || :query || '%'
            OR cardIssuerId LIKE '%' || :query || '%') AND date LIKE '%' || :date || '%'
            """ )
    suspend fun getPaymentsByDate(query:String, date:String): List<PaymentEntity>

}