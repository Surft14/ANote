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
        return repository.getAllNotes()
    }

    // Функция для добавления заметки
    fun insertNote(note: Note) {
        viewModelScope.launch {
            val result = repository.insertNote(note)
            when (result){
                is OperationResult.Success -> Log.d("MyLog", "insertNote success")
                is OperationResult.Error -> Log.d("MyLog", "insertNote error: ${result.exception.message}}")
            }
        }
    }

    // Функция для удаления заметки
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            val result = repository.deleteNote(note)
            when (result){
                is OperationResult.Success -> Log.d("MyLog", "deleteNote success")
                is OperationResult.Error -> Log.d("MyLog", "deleteNote error: ${result.exception.message}}")
            }
        }
    }

    // Функция для обновления заметки
    fun updateNote(note: Note) {
        viewModelScope.launch {
            val result = repository.updateNote(note)
            when (result){
                is OperationResult.Success -> Log.d("MyLog", "updateNote success")
                is OperationResult.Error -> Log.d("MyLog", "updateNote error: ${result.exception.message}}")
            }
        }
    }




}