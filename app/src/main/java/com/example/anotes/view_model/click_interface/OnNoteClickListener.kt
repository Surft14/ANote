package com.example.anotes.view_model.click_interface

import com.example.anotes.datebase.db_notes.Note

interface OnNoteClickListener {
    fun onNoteClick(note: Note)// Обработка клика по элемента
    fun onNoteLongClick(note: Note): Boolean// Обработка долгого клика по элементу
}