package com.example.anotes

import android.app.Application
import android.util.Log
import com.example.anotes.datebase.db_notes.NDatabaseProvider

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Инициализируем базу данных здесь
        Log.d("MyLog", "MyApplication: onCreate")
        NDatabaseProvider.initializeDatabase(this)
    }

}