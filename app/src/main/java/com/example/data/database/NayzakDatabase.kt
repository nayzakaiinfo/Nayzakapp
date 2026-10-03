package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.dao.CharacterDao
import com.example.data.database.dao.GameRoundDao
import com.example.data.database.dao.QuoteDao
import com.example.data.database.entity.CharacterEntity
import com.example.data.database.entity.GameRoundEntity
import com.example.data.database.entity.QuoteEntity

@Database(
    entities = [
        CharacterEntity::class,
        QuoteEntity::class,
        GameRoundEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NayzakDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun quoteDao(): QuoteDao
    abstract fun gameRoundDao(): GameRoundDao

    companion object {
        @Volatile
        private var INSTANCE: NayzakDatabase? = null

        fun getInstance(context: Context): NayzakDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NayzakDatabase::class.java,
                    "nayzak_game.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
