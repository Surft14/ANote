package com.example.anotes.model

import android.graphics.Path.Op
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.datebase.db_category.CategoryDao
import com.example.anotes.datebase.db_favorite.Favorite
import com.example.anotes.datebase.db_favorite.FavoriteDao
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.datebase.db_notes.NoteDao
//Единая точка сопрекосновеняи с данными из базой данных
@Suppress("UNREACHABLE_CODE")
class NoteRepository(private val noteDao : NoteDao, private val categoryDao: CategoryDao) {
    //Функции с Избранным
    //Получения всй таблицы
    /*fun getAllFavorites(): LiveData<List<Favorite>> {
        Log.d("MyLog", "NoteRepository: getAllFavorites")
        return favoriteDao.getAllFavorites()
    }
    //Добовление новой
    suspend fun insertFavorite(favorite: Favorite): OperationResult{
        Log.d("MyLog", "NoteRepository: insertFavorite")
        val newId: Long
        return try {
            newId = favoriteDao.insertFavorite(favorite)
            Log.i("MyLog", "NoteRepository: newID - $newId")
            return OperationResult.Success(newId.toInt())
        }
        catch (e: Exception){
            return OperationResult.Error(e)
        }
    }
    //Удаляем из избранного
    suspend fun deleteFavorite(favorite: Favorite): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteFavorite")
        return try {
            favoriteDao.deleteFavorite(favorite)
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    //Удоляем заметки из избранного
    suspend fun deleteFavorites(favoriteIds: List<Int?>): OperationResult {
        Log.d("MyLog", "NoteRepository: deleteFavorites")
        return try {
            favoriteDao.deleteFavorites(favoriteIds)
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    //Удаляем все заметки из избранного
    suspend fun deleteAllFavorites(): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteAllFavorites")
        return try {
            favoriteDao.clearFavorites()
            favoriteDao.resetAutoIncrementFavorite()
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    //Обноляем избранное
    suspend fun updateFavorite(favorite: Favorite): OperationResult{
        Log.d("MyLog", "NoteRepository: updateFavorite")
        return try {
            favoriteDao.updateFavorite(favorite)
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
*/
    //Функции для таблицы с категориями
    //получаем все категории
    fun getAllCategorys(): LiveData<List<Category>>{
        Log.d("MyLog", "NoteRepository: getAllCategorys")
        return categoryDao.getAllCategorys()
    }
    // добавление новой категории
    suspend fun insertCategory(category: Category): OperationResult{
       val newId: Long
        Log.d("MyLog", "NoteRepository: insertCategory")
        try {
            newId = categoryDao.insertCategory(category)
            Log.i("MyLog", "NoteRepository: newID - $newId")
            return OperationResult.Success(newId.toInt())
        }
        catch (e: Exception){
            return OperationResult.Error(e)// Возвращаем ошибку
        }
    }
    //Удаляем категорию
    suspend fun deleteCategory(category: Category): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteCategory")
        return try {
            categoryDao.deleteCategory(category)
            OperationResult.Success(-1)// Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e)// Возвращаем ошибку
        }
    }
    //Удаляем категории
    suspend fun deleteCategorys(categoryIds: List<Int?>): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteCategorys")
        return try {
            categoryDao.deleteCategorys(categoryIds)
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    //Удаляем все категории
    suspend fun deleteAllCategorys(): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteAllCategorys")
        return try {
            categoryDao.clearCategorys()
            categoryDao.resetAutoIncrementCategory()
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    // Обновляем данные
    suspend fun updateCategory(category: Category): OperationResult{
        Log.d("MyLog", "NoteRepository: updateCategory")
        return try {
            categoryDao.updateCategory(category)
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }

    //Функции для таблицы с заметками
    //Получаме всех данных
    fun getAllNotes(): LiveData<List<Note>> {
        Log.d("MyLog", "NoteRepository: getAllNotes")
        return noteDao.getAllNotes()
    }
    //вставляет новую информацию
    suspend fun insertNote(note: Note): OperationResult {
        val newId: Long
        Log.d("MyLog", "NoteRepository: insertNote")
        try {
            newId = noteDao.insertNote(note)
            Log.i("MyLog", "NoteRepository: newID - $newId")
            return OperationResult.Success(newId.toInt())// Операция прошла успешно
        }
        catch (e: Exception){
            return OperationResult.Error(e)// Возвращаем ошибку
        }

    }
    // Удаление заметки в базе
    suspend fun deleteNote(note: Note): OperationResult {
        Log.d("MyLog", "NoteRepository: deleteNote")
        return try{
            noteDao.deleteNote(note)
            OperationResult.Success(-1) // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }
    // Удаление заметок
    suspend fun deleteNotes(noteIds: List<Int?>): OperationResult {
        Log.d("MyLog", "NoteRepository: deleteNotes")
        return try {
            noteDao.deleteNotes(noteIds)
            OperationResult.Success(-1)
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
    // Удаление всех заметок
    suspend fun deleteAllNotesFromDB(): OperationResult{
        Log.d("MyLog", "NoteRepository: deleteAllNotesFromDB")
        return try {
            noteDao.clearNotes()
            noteDao.resetAutoIncrementNote()
            OperationResult.Success(-1)
        }
        catch (e: Exception){
            OperationResult.Error(e)
        }
    }
    //Обновление данных в базе
    suspend fun updateNote(note: Note): OperationResult{
        Log.d("MyLog", "NoteRepository: updateNote")
        return  try {
            noteDao.updateNote(note)
            OperationResult.Success(-1) // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }
    //Ищем заметки
    fun searchNotes(searchQuery: String): LiveData<List<Note>>{
        Log.d("MyLog", "NoteRepository: searchNotes")
        return try {
            Log.i("MyLog", "NoteRepository: searchNotes success")
            noteDao.searchNotes(searchQuery)
        }
        catch (e: Exception){
            Log.e("MyLog", "NoteRepository: searchNotes error ${e.message}")
            TODO()
        }
    }
    // Получаем все избранные заметки
    fun getFavoriteListNote(): LiveData<List<Note>>{
        Log.d("MyLog", "NoteRepository: getFavoriteListNote")
        return noteDao.getFavoriteListNote()
    }
}