package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.entity.GameRoundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRoundDao {
    @Query("SELECT * FROM game_rounds ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentRounds(limit: Int): Flow<List<GameRoundEntity>>

    @Query("SELECT MAX(score) FROM game_rounds")
    fun getPersonalBestScoreFlow(): Flow<Int?>

    @Query("SELECT MAX(score) FROM game_rounds")
    suspend fun getPersonalBestScore(): Int?

    @Query("SELECT COUNT(*) FROM game_rounds")
    fun getTotalRoundsPlayedFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_rounds")
    suspend fun getTotalRoundsPlayed(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRound(round: GameRoundEntity)
}
