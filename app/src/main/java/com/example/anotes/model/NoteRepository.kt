package com.example.anotes.model

import android.graphics.Path.Op
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.anotes.datebase.db_category.CategoryDao
import com.example.anotes.datebase.db_favorite.FavoriteDao
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.datebase.db_notes.NoteDao
//Единая точка сопрекосновеняи с данными из базой данных
@Suppress("UNREACHABLE_CODE")
class NoteRepository(private val noteDao : NoteDao, private val categoryDao: CategoryDao, private val favoriteDao: FavoriteDao) {


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
            newId = noteDao.insert(note)
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
            noteDao.delete(note)
            OperationResult.Success(-1) // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }
    // Удаление заметок
    suspend fun deleteNotes(noteIds: List<Int?>): OperationResult {
        return try {
            noteDao.deleteNotes(noteIds)
            OperationResult.Success(-1)
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
    // Удаление всех заметок
    suspend fun deleteAllNotesFromDB(): OperationResult{
        return try {
            noteDao.clearNotes()
            noteDao.resetAutoIncrement()
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
            noteDao.update(note)
            OperationResult.Success(-1) // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }

}


