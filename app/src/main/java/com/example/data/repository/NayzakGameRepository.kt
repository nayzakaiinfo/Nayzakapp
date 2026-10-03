package com.example.data.repository

import android.content.Context
import com.example.data.HistoricalFigure
import com.example.data.QuizQuestion
import com.example.data.QuoteItem
import com.example.data.database.DatabaseSeedData
import com.example.data.database.NayzakDatabase
import com.example.data.database.entity.GameRoundEntity
import com.example.data.database.entity.QuoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

data class ExplorationProgress(
    val seenQuotesCount: Int,
    val totalQuotesCount: Int,
    val masteredQuotesCount: Int,
    val totalRoundsPlayed: Int,
    val personalBestScore: Int
)

class NayzakGameRepository(context: Context) {

    private val db = NayzakDatabase.getInstance(context)
    private val characterDao = db.characterDao()
    private val quoteDao = db.quoteDao()
    private val gameRoundDao = db.gameRoundDao()

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        val count = quoteDao.getTotalQuotesCount()
        if (count == 0) {
            characterDao.insertAll(DatabaseSeedData.characters)
            quoteDao.insertAll(DatabaseSeedData.quotes)
        }
    }

    /**
     * Smart question generation:
     * 1. Prioritizes UNSEEN quotes (timesSeen == 0) in the selected category or all.
     * 2. If unseen quotes are fewer than [count], fills the rest with least-recently seen quotes.
     * 3. Completely prevents question repetition until the player has explored the entire quote bank!
     */
    suspend fun generateSmartRound(
        category: String = "الكل",
        count: Int = 10
    ): List<QuizQuestion> = withContext(Dispatchers.IO) {
        initializeIfNeeded()

        val isAll = category == "الكل" || category.isBlank()

        // 1. Fetch unseen quotes first
        val unseenQuotes = if (isAll) {
            quoteDao.getUnseenQuotes(count)
        } else {
            quoteDao.getUnseenQuotesByCategory(category, count)
        }

        val selectedEntities = mutableListOf<QuoteEntity>()
        selectedEntities.addAll(unseenQuotes)

        // 2. If we need more, fetch least-seen and oldest-seen quotes
        if (selectedEntities.size < count) {
            val needed = count - selectedEntities.size
            val fallbackQuotes = if (isAll) {
                quoteDao.getLeastSeenQuotes(count * 2)
            } else {
                quoteDao.getLeastSeenQuotesByCategory(category, count * 2)
            }

            for (fb in fallbackQuotes) {
                if (selectedEntities.none { it.id == fb.id }) {
                    selectedEntities.add(fb)
                    if (selectedEntities.size == count) break
                }
            }
        }

        // 3. Fallback safety if database is somehow empty
        if (selectedEntities.isEmpty()) {
            selectedEntities.addAll(DatabaseSeedData.quotes.shuffled().take(count))
        }

        // 4. Build QuizQuestion objects with 4 options (1 correct + 3 wrong options)
        selectedEntities.map { entity ->
            val correctFigure = HistoricalFigure.fromId(entity.authorId)

            // Select 3 distinct wrong figures, preferring the same category when possible
            val sameCategoryOthers = HistoricalFigure.entries
                .filter { it != correctFigure && (isAll || it.categoryAr == correctFigure.categoryAr) }
            val otherPool = if (sameCategoryOthers.size >= 3) {
                sameCategoryOthers
            } else {
                HistoricalFigure.entries.filter { it != correctFigure }
            }

            val wrongFigures = otherPool.shuffled().take(3)
            val options = (wrongFigures + correctFigure).shuffled()

            val quoteItem = QuoteItem(
                id = entity.id.hashCode(),
                quote = entity.quote,
                author = entity.authorName,
                figureId = entity.authorId,
                category = entity.category,
                hint = entity.hint,
                funFact = entity.funFact
            )

            QuizQuestion(
                quoteItem = quoteItem,
                options = options,
                correctFigure = correctFigure
            )
        }
    }

    suspend fun recordAnswer(quoteId: String, wasCorrect: Boolean) = withContext(Dispatchers.IO) {
        quoteDao.updateQuoteStats(
            quoteId = quoteId,
            wasCorrectInt = if (wasCorrect) 1 else 0,
            timestamp = System.currentTimeMillis()
        )
    }

    suspend fun recordGameRound(
        category: String,
        score: Int,
        correctCount: Int,
        maxStreak: Int
    ) = withContext(Dispatchers.IO) {
        gameRoundDao.insertRound(
            GameRoundEntity(
                timestamp = System.currentTimeMillis(),
                category = category,
                score = score,
                correctCount = correctCount,
                maxStreak = maxStreak
            )
        )
    }

    suspend fun getExplorationProgress(): ExplorationProgress = withContext(Dispatchers.IO) {
        initializeIfNeeded()
        val total = quoteDao.getTotalQuotesCount()
        val seen = quoteDao.getSeenQuotesCount()
        val rounds = gameRoundDao.getTotalRoundsPlayed()
        val best = gameRoundDao.getPersonalBestScore() ?: 0

        ExplorationProgress(
            seenQuotesCount = seen,
            totalQuotesCount = total,
            masteredQuotesCount = 0,
            totalRoundsPlayed = rounds,
            personalBestScore = best
        )
    }

    suspend fun resetQuestionHistory() = withContext(Dispatchers.IO) {
        quoteDao.resetAllStats()
    }
}
