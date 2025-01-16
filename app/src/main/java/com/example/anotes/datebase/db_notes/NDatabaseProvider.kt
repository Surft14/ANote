package com.example.anotes.datebase.db_notes

import android.content.Context
import android.util.Log
import androidx.room.Room
//Через него получаем доступ к базе и инициализируем базу данных
object NDatabaseProvider {
    private var instance: NAppDatabase? = null

    fun initializeDatabase(context: Context){
        Log.d("MyLog", "DatabaseProvider: Starting database initialization")
        if (instance == null){
            Log.d("MyLog", "DatabaseProvider: Synchronization block entered")
            synchronized(NAppDatabase::class){// Синхронизация для потокабезопасности
                Log.d("MyLog", "DatabaseProvider: start databaseBuilder")
                instance = Room.databaseBuilder(// Создание экземпляра базы данных
                    context.applicationContext,// Использует глобальный контекст приложения
                    NAppDatabase::class.java,// Указывает класс базы данных
                    "app_db_notes",// Имя базы данных
                )
                    .fallbackToDestructiveMigration()
                    .build()
                Log.d("MyLog", "DatabaseProvider: Database built successfully")
            }
        }
    }
    fun getDatabase(): NAppDatabase {
        Log.d("MyLog", "DatabaseProvider: getDatabase")
        return instance ?: throw IllegalStateException("Database not initialized. Call initialize() first.")
    }
}