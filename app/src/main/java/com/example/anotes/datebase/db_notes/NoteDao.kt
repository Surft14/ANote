package com.example.anotes.datebase.db_notes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao//DAO (Data Access Object - Объект доступа к данным) здесь я описал интерфейс для SQl запросов
//Каждый метод должен быть связанн с SQL запросами через аннотацию
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timeStamp DESC")//SQL запрос
    fun getAllNotes(): LiveData<List<Note>>//Получить все заметки

    @Insert(onConflict = OnConflictStrategy.REPLACE)//SQL запрос Если будет заметка с такимже id то произайдет замена
    suspend fun insertNote(note: Note): Long//Добавить новую заметку

    @Update//SQL запрос
    suspend fun updateNote(note: Note)//Обноление существующей заметки

    @Delete//SQL запрос
    suspend fun deleteNote(note: Note)//Удаление заметки


    @Query("DELETE FROM notes WHERE id IN (:noteIds)")//SQL запрос
    suspend fun deleteNotes(noteIds: List<Int?>)//Удаление всех заметок

    @Query("DELETE FROM notes")
    suspend fun clearNotes()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'notes'")
    suspend fun resetAutoIncrementNote()


}