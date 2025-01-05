package com.example.anotes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anotes.db_notes.Note
import com.example.anotes.model.NoteRepository
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: NoteRepository):ViewModel() {

    // Получение списка заметок через LiveData
    val allNotes: LiveData<List<Note>> = repository.getAllNotes()

    // Функция для добавления заметки
    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    // Функция для удаления заметки
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    // Функция для обновления заметки
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }




}