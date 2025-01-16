package com.example.anotes.datebase.db_notes

import androidx.room.Database
import androidx.room.RoomDatabase
//База данных объединяет сущности и DAO. Это класс, который наследует RoomDatabase
@Database(entities = [Note::class], version = 3)//Указывает, какие таблицы используются в базе данных и версия базы данных если
// измениться то версию нужно будет увеличить
abstract class NAppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao//Ссылка на DAO
}
