package com.example.anotes.db_notes

import java.time.LocalDate
import java.time.LocalTime
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")//Это аннотация специальная метка добовляющая метаинформацию к классу или методу
//Он создает таблице с именим notes
data class Note(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,//Первичный ключ с авто инкриментом
    val title: String,//Столбец для загаловка
    val content: String,//Столбец для содержания
    val date: LocalDate,//Столбец для даты
    val time: LocalTime,//Столбец для времени
    val timeStamp: Long,//Столбец для времени
    val listTag: ArrayList<String>? = null,//Столбец для списка тегов по умолчанию их нет
    val category: String = "general",//Столбец для категорий по умолчанию general
)
