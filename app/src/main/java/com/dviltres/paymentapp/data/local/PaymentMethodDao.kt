package com.dviltres.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dviltres.paymentapp.data.local.entity.PaymentMethodEntity

@Dao
interface PaymentMethodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(paymentMethodEntity: PaymentMethodEntity)

    @Query("""
        SELECT * FROM PaymentMethodEntity 
        WHERE name LIKE '%' || :query || '%'
        LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun getPaymentMethods(query:String, page:Int, pageSize:Int): List<PaymentMethodEntity>

    @Query("SELECT * FROM PaymentMethodEntity Where id=:paymentId")
    suspend fun getPaymentMethodById(paymentId:String): PaymentMethodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethods(items: List<PaymentMethodEntity>)

    @Query("DELETE FROM PaymentMethodEntity")
    suspend fun deletePaymentMethods()

    @Query("SELECT COUNT(*) FROM PaymentMethodEntity")
    suspend fun getCantPaymentMethods(): Int
}