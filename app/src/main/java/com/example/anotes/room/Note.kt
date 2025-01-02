package com.example.anotes.room

import java.time.LocalDate
import java.time.LocalTime
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val title: String,
    val content: String,
    val date: LocalDate,
    val time: LocalTime,
    val timeStamp: Long,
    val listTag: ArrayList<String>?,
    val category: String = "general",
)
