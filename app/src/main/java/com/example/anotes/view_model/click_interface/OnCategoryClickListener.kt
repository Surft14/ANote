package com.example.anotes.view_model.click_interface

import com.example.anotes.datebase.db_category.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)// Обработка клика по элемента
    fun onCategoryLongClick(category: Category): Boolean// Обработка долгого клика по элементу
}