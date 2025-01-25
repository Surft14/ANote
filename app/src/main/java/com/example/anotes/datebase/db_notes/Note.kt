package com.example.anotes.datebase.db_notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")//Это аннотация специальная метка добовляющая метаинформацию к классу или методу
//Он создает таблице с именим notes
data class Note(
    @PrimaryKey(autoGenerate = true)val id: Int? = null,//Первичный ключ с авто инкриментом
    var title: String,//Столбец для загаловка
    var content: String,//Столбец для содержания
    val date: String,//Столбец для даты
    val time: String,//Столбец для времени
    val timeStamp: Long,//Столбец для времени
    var listTag: String? = null,//Столбец для списка тегов по умолчанию их нет
    var category: String = "general",//Столбец для категорий по умолчанию general
    var favorite: Boolean = false
): Serializable
