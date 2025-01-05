package com.example.anotes.model

import android.content.Context
import com.example.anotes.db_notes.DatabaseProvider
import com.example.anotes.db_notes.Note
import kotlinx.coroutines.flow.Flow
//Единая точка сопрекосновеняи с данными из базой данных
class NoteRepository(private val context: Context) {

    private val noteDao = DatabaseProvider.getDatabase(context).noteDao()
    //Получаме всех данных
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }
    //вставляет новую информацию
    suspend fun insertNote(note: Note): OperationResult {
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
        return  try {
            noteDao.update(note)
            OperationResult.Success // Операция прошла успешно
        }
        catch (e: Exception){
            OperationResult.Error(e) // Возвращаем ошибку
        }
    }

}

//Пример обработки
/*
val result = repository.insertNote(note)
when (result) {
    is OperationResult.Success -> println("Операция прошла успешно")
    is OperationResult.Error -> println("Ошибка: ${result.exception.message}")
}*/
