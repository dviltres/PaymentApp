package com.dviltres.paymentapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dviltres.paymentapp.data.local.entity.CardIssuerEntity
import com.dviltres.paymentapp.data.local.entity.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardIssuerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardIssuer(cardIssuerEntity: CardIssuerEntity)

    @Query("SELECT * FROM CardIssuerEntity")
    suspend fun getCardIssuers(): List<CardIssuerEntity>

    @Query("SELECT * FROM CardIssuerEntity Where id=:cardIssuerId")
    suspend fun getCardIssuerById(cardIssuerId:String): CardIssuerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardIssuers(items: List<CardIssuerEntity>)

    @Query("SELECT COUNT(*) FROM CardIssuerEntity")
    suspend fun getCantCardIssuer(): Int

    @Query("DELETE FROM CardIssuerEntity")
    suspend fun deleteCardIssuers()
}