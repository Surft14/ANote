package com.example.anotes.room

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        if (instance == null){
            synchronized(AppDatabase::class){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database",
                ).build()
            }
        }
        return instance!!
    }
}