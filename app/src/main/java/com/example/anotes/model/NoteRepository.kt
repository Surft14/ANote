package com.example.anotes.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.anotes.db_notes.Note
import com.example.anotes.db_notes.NoteDao
//Единая точка сопрекосновеняи с данными из базой данных
class NoteRepository(private val noteDao : NoteDao) {


    //Получаме всех данных
    fun getAllNotes(): LiveData<List<Note>> {
        Log.d("MyLog", "NoteRepository: getAllNotes")
        return noteDao.getAllNotes()
    }
    //вставляет новую информацию
    suspend fun insertNote(note: Note): OperationResult {
        Log.d("MyLog", "NoteRepository: insertNote")
        return try {
            noteDao.insert(note)
            OperationResult.Success // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e)// Возвращаем ошибку
        }

    }
    // Удаление заметки в базе
    suspend fun deleteNote(note: Note): OperationResult {
        Log.d("MyLog", "NoteRepository: deleteNote")
        return try{
            noteDao.delete(note)
            OperationResult.Success // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }
    //Обновление данных в базе
    suspend fun updateNote(note: Note): OperationResult{
        Log.d("MyLog", "NoteRepository: updateNote")
        return  try {
            noteDao.update(note)
            OperationResult.Success // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }

}


