package com.dviltres.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dviltres.paymentapp.data.local.entity.CardIssuerEntity
import com.dviltres.paymentapp.data.local.entity.InstallmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InstallmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallment(installmentEntity: InstallmentEntity)

    @Query("SELECT * FROM InstallmentEntity Where payment_method_id=:paymentMethodId")
    suspend fun getInstallments(paymentMethodId: String):InstallmentEntity?

    @Query("SELECT * FROM InstallmentEntity Where issuerId=:issuerId")
    suspend fun getInstallmentById(issuerId:String): InstallmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstallments(items: List<InstallmentEntity>)

    @Query("SELECT COUNT(*) FROM InstallmentEntity")
    suspend fun getCantInstallment(): Int

    @Query("DELETE FROM InstallmentEntity")
    suspend fun deleteInstallments()

}