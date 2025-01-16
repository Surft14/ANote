package com.example.anotes

import android.app.Application
import android.util.Log
import com.example.anotes.datebase.DatabaseProvider

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Инициализируем базу данных здесь
        Log.d("MyLog", "MyApplication: onCreate")
        DatabaseProvider.initializeDatabase(this)
    }

}