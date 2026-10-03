package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey
    val id: String,
    val quote: String,
    val authorId: String,
    val authorName: String,
    val category: String,
    val hint: String,
    val funFact: String,
    val difficulty: Int = 1,
    val timesSeen: Int = 0,
    val timesCorrect: Int = 0,
    val lastSeenAt: Long = 0L
)
