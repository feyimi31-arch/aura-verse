package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlayerEntity::class], version = 1, exportSchema = false)
abstract class AuraverseDatabase : RoomDatabase() {
    abstract fun auraverseDao(): AuraverseDao

    companion object {
        @Volatile
        private var INSTANCE: AuraverseDatabase? = null

        fun getDatabase(context: Context): AuraverseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuraverseDatabase::class.java,
                    "auraverse_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
