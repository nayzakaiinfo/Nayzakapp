package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes")
    suspend fun getAllQuotesSync(): List<QuoteEntity>

    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun getTotalQuotesCount(): Int

    @Query("SELECT COUNT(*) FROM quotes WHERE timesSeen > 0")
    fun getSeenQuotesCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM quotes WHERE timesSeen > 0")
    suspend fun getSeenQuotesCount(): Int

    @Query("SELECT COUNT(*) FROM quotes WHERE timesCorrect > 0")
    fun getMasteredQuotesCountFlow(): Flow<Int>

    // --- Smart Unseen Quotes Retrieval ---
    @Query("SELECT * FROM quotes WHERE timesSeen == 0 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getUnseenQuotes(limit: Int): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE timesSeen == 0 AND category = :category ORDER BY RANDOM() LIMIT :limit")
    suspend fun getUnseenQuotesByCategory(category: String, limit: Int): List<QuoteEntity>

    // --- Least-Seen & Oldest-Seen Retrieval (Fallback when all are seen) ---
    @Query("SELECT * FROM quotes ORDER BY timesSeen ASC, lastSeenAt ASC, RANDOM() LIMIT :limit")
    suspend fun getLeastSeenQuotes(limit: Int): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY timesSeen ASC, lastSeenAt ASC, RANDOM() LIMIT :limit")
    suspend fun getLeastSeenQuotesByCategory(category: String, limit: Int): List<QuoteEntity>

    @Query("SELECT DISTINCT category FROM quotes ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quotes: List<QuoteEntity>)

    @Query("UPDATE quotes SET timesSeen = timesSeen + 1, timesCorrect = timesCorrect + :wasCorrectInt, lastSeenAt = :timestamp WHERE id = :quoteId")
    suspend fun updateQuoteStats(quoteId: String, wasCorrectInt: Int, timestamp: Long)

    @Query("UPDATE quotes SET timesSeen = 0, timesCorrect = 0, lastSeenAt = 0")
    suspend fun resetAllStats()
}
