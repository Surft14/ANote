package com.example.anotes.datebase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.datebase.db_category.CategoryDao
import com.example.anotes.datebase.db_favorite.Favorite
import com.example.anotes.datebase.db_favorite.FavoriteDao
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.datebase.db_notes.NoteDao

//База данных объединяет сущности и DAO. Это класс, который наследует RoomDatabase
@Database(entities = [Note::class, Category::class], version = 11)//Указывает, какие таблицы используются в базе данных и версия базы данных если
// измениться то версию нужно будет увеличить
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao//Ссылка на DAO
    abstract fun categoryDao(): CategoryDao// Ссылка на категории
//    abstract fun favoriteDao(): FavoriteDao // ссылка на избранное Dao
}
