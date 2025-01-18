package com.example.anotes.datebase.db_category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categorys ORDER BY timeStamp DESC")//SQL запрос

    fun getAllCategorys(): LiveData<List<Category>>//Получить все заметки

    @Insert(onConflict = OnConflictStrategy.REPLACE)//SQL запрос Если будет заметка с такимже id то произайдет замена
    suspend fun insertCategory(category: Category): Long//Добавить новую заметку

    @Update//SQL запрос
    suspend fun updateCategory(category: Category)//Обноление существующей заметки

    @Delete//SQL запрос
    suspend fun deleteCategory(category: Category)//Удаление заметки

    @Query("DELETE FROM categorys WHERE id IN (:categoryIds)")//SQL запрос
    suspend fun deleteCategorys(categoryIds: List<Int?>)//Удаление всех заметок

    @Query("DELETE FROM categorys")
    suspend fun clearCategorys()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'favorites'")
    suspend fun resetAutoIncrementCategory()
}