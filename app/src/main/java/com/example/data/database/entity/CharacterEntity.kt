package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val titleAr: String,
    val eraAr: String,
    val category: String,
    val colorHex: Long,
    val avatarEmoji: String,
    val reactionCorrect: String,
    val reactionWrong: String
)
