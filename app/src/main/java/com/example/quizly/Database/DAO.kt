
package com.example.quizly.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<Card>>  // ✅ Corrected reference

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Query("UPDATE cards SET value = :newValue WHERE id = :cardId")
    suspend fun updateCardValue(cardId: Int, newValue: String)
}
