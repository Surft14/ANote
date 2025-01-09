package com.example.anotes.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anotes.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.model.OperationResult
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: NoteRepository):ViewModel() {

    // Получение списка заметок через LiveData

    fun getAllNotes(): LiveData<List<Note>>{
        Log.d("MyLog", "NoteViewModel: getAllNotes")
        return repository.getAllNotes()
    }

    // Функция для добавления заметки
    fun insertNote(note: Note){
        Log.d("MyLog", "NoteViewModel: insertNote start")
        viewModelScope.launch {
           val result = repository.insertNote(note)
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: insertNote success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: insertNote error: ${result.exception.message}}")
                }
            }
        }
    }

    // Функция для удаления заметки
    fun deleteNote(note: Note) {
        Log.d("MyLog", "NoteViewModel: deleteNote start")
        viewModelScope.launch {
            val result = repository.deleteNote(note)
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: deleteNote success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: deleteNote error: ${result.exception.message}}")
                }
            }
        }
    }

    // Функция для обновления заметки
    fun updateNote(note: Note) {
        Log.d("MyLog", "NoteViewModel: updateNote start")
        viewModelScope.launch {
            val result = repository.updateNote(note)
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: updateNote success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: updateNote error: ${result.exception.message}}")
                }
            }
        }
    }




}