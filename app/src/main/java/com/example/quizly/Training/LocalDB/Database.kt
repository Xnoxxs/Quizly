
package com.example.quizly.Training.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null  // ✅ Use your own class name

        fun getInstance(context: Context): AppDatabase {     // ✅ No need for long package reference
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "quizly_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
