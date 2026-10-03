package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_rounds")
data class GameRoundEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val category: String,
    val score: Int,
    val correctCount: Int,
    val maxStreak: Int
)
