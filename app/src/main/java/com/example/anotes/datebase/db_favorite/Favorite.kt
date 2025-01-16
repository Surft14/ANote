package com.example.anotes.datebase.db_favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.anotes.datebase.db_notes.Note

@Entity(tableName = "favorites")
data class Favorite (
    @PrimaryKey(autoGenerate = true)val id: Int? = null,
    var note: Note,
    val date: String,//Столбец для даты
    val time: String,//Столбец для времени
    val timeStamp: Long,//Столбец для времени
)