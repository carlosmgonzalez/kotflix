package com.carlosmgonzalez.kotflix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, MovieCastEntity::class, TvSeriesEntity::class],
    version = 8,
    exportSchema = false
)
abstract class KotflixDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var Instance: KotflixDatabase? = null
        fun getDatabase(context: Context): KotflixDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = KotflixDatabase::class.java,
                    name = "kotflix_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it}
            }
        }
    }
}