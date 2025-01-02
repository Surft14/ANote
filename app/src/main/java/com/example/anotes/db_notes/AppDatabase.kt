package com.example.anotes.db_notes

import androidx.room.Database
import androidx.room.RoomDatabase
//База данных объединяет сущности и DAO. Это класс, который наследует RoomDatabase
@Database(entities = [Note::class], version = 1)//Указывает, какие таблицы используются в базе данных и версия базы данных если измениться то версию нужно будет увеличить
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao//Ссылка на DAO
}
