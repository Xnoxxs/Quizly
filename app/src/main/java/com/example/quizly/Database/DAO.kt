
package com.example.quizly.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Query("UPDATE cards SET card = :newCard, value = :newValue WHERE id = :id")
    suspend fun updateCard(id: Int, newCard: String, newValue: String)

    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: Int): Card?


    @Query("DELETE FROM cards WHERE id = :cardId")
    suspend fun deleteCardById(cardId: Int)




}
