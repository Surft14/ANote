package com.example.anotes.db_notes

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao//DAO (Data Access Object - Объект доступа к данным) здесь я описал интерфейс для SQl запросов
//Каждый метод должен быть связанн с SQL запросами через аннотацию
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timeStamp DESC")//SQL запрос
    fun getAllNotes(): Flow<List<Note>>//Получить все заметки

    @Insert(onConflict = OnConflictStrategy.REPLACE)//SQL запрос Если будет заметка с такимже id то произайдет замена
    suspend fun insert(note: Note)//Добавить новую заметку

    @Update//SQL запрос
    suspend fun update(note: Note)//Обноление существующей заметки

    @Delete//SQL запрос
    suspend fun delete(note: Note)//Удаление заметки
}