package com.example.anotes.datebase.db_favorite

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY timeStamp DESC")//SQL запрос
    fun getAllFavorites(): LiveData<List<Favorite>>//Получить все заметки

    @Insert(onConflict = OnConflictStrategy.REPLACE)//SQL запрос Если будет заметка с такимже id то произайдет замена
    suspend fun insertFavorite(favorite: Favorite): Long//Добавить новую заметку

    @Update//SQL запрос
    suspend fun updateFavorite(favorite: Favorite)//Обноление существующей заметки

    @Delete//SQL запрос
    suspend fun deleteFavorite(favorite: Favorite)//Удаление заметки

    @Query("DELETE FROM favorites WHERE id IN (:favoritesIds)")//SQL запрос
    suspend fun deleteFavorites(favoritesIds: List<Int?>)//Удаление всех заметок

    @Query("DELETE FROM favorites")
    suspend fun clearFavorites()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'favorites'")
    suspend fun resetAutoIncrementFavorite()
}