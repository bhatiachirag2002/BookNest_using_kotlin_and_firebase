package com.example.budgettracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bhatia.booknest.db.PdfDao
import com.bhatia.booknest.model.PdfEntity


@Database(entities = [PdfEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun getPdfDao(): PdfDao

    companion object {
        @Volatile
        private var instance: AppDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java,
                name = "booknest"
            ).build()
    }
}