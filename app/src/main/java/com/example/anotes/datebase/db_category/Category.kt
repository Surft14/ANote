package com.example.anotes.datebase.db_category

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
//Сущность категория для создания таблицы
@Entity(tableName = "categorys")
data class Category (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,//id категории
    val category: String,// сама категория
    val date: String,//Столбец для даты
    val time: String,//Столбец для времени
    val timeStamp: Long,//Столбец для времени
): Serializable