package com.example.anotes.view_model.click_interface

import com.example.anotes.datebase.db_favorite.Favorite

interface OnFavoriteClickListener {
    fun onFavoriteClick(favorite: Favorite)// Обработка клика по элемента
    fun onFavoriteLongClick(favorite: Favorite): Boolean// Обработка долгого клика по элементу
}