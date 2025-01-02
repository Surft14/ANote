package com.example.anotes.db_notes

import android.content.Context
import androidx.room.Room
//
object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        if (instance == null){
            synchronized(AppDatabase::class){//Синхронизация для потокабезопасности
                instance = Room.databaseBuilder(//Создание экземпляра базы данных
                    context.applicationContext,//Использует глобальный контекст приложения
                    AppDatabase::class.java,//Указывает класс базы данных
                    "app_db_notes",// Имя базы данных
                ).build()
            }
        }
        return instance!!
    }
}