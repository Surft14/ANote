package com.example.anotes.room

import androidx.room.*
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timeStamp DESC")
    fun getAllNotes(): List<Note>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)
    @Update
    fun update(note: Note)
    @Delete
    fun delete(id: Int)
}