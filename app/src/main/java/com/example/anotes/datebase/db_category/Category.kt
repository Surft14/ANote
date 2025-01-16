package com.example.anotes.datebase.db_category

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "categorys")
//Сущность категория для создания таблицы
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,//id категории
    val category: String,// сама категория
    val date: String,//Столбец для даты
    val time: String,//Столбец для времени
    val timeStamp: Long,//Столбец для времени
): Serializable