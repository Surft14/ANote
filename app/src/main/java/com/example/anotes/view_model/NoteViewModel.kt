package com.example.anotes.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.anotes.datebase.db_category.Category
import com.example.anotes.datebase.db_favorite.Favorite
import com.example.anotes.datebase.db_notes.Note
import com.example.anotes.model.NoteRepository
import com.example.anotes.model.OperationResult
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: NoteRepository):ViewModel() {
    //Храниться результат запроса
    private val _insertResult = MutableLiveData<OperationResult>()
    val insertResult: LiveData<OperationResult> = _insertResult

    //Избранное
    //Получение всех категорий
/*    fun getAllFavorites(): LiveData<List<Favorite>>{
        Log.d("MyLog", "NoteViewModel: getAllFavorites")
        return repository.getAllFavorites()
    }
    //Добовляет категорию
    fun insertFavorite(favorite: Favorite){
        Log.d("MyLog", "NoteViewModel: insertFavorite start")
        viewModelScope.launch {
            val result = repository.insertFavorite(favorite)
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: insertFavorite success")
                }
                is OperationResult.Error -> {
                    Log.e("MyLog", "NoteViewModel: insertFavorite error: ${result.exception.message}}")
                }
            }
        }
    }
    //Функция для удаления категории
    fun deleteFavorite(favorite: Favorite){
        Log.d("MyLog", "NoteViewModel: deleteFavorite start")
        viewModelScope.launch {
            val result = repository.deleteFavorite(favorite)
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: deleteFavorite success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: deleteFavorite error: ${result.exception.message}}")
                }
            }
        }
    }
    //Удаление категорий
    suspend fun deleteFavorites(favorites: List<Favorite>){
        Log.d("MyLog", "NoteViewModel: deleteCategorys start")
        val favoriteIds = favorites.map { it.id }
        val result = repository.deleteCategorys(favoriteIds)
        _insertResult.value = result
        when (result) {
            is OperationResult.Success -> Log.d("MyLog", "NoteViewModel: deleteFavorites success")
            is OperationResult.Error -> Log.d("MyLog", "NoteViewModel: deleteFavorites error: ${result.exception.message}")
        }
    }
    // Удаление всех категорий
    fun deleteAllFavorites(){
        Log.d("MyLog", "NoteViewModel: deleteAllCategorys start")
        viewModelScope.launch {
            val result = repository.deleteAllFavorites()
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> Log.d("MyLog", "NoteViewModel: deleteAllFavorites success")
                is OperationResult.Error -> Log.d("MyLog", "NoteViewModel: deleteAllFavorites error: ${result.exception.message}")
            }
        }
    }
    // Обновление категорий
    fun updateFavorite(favorite: Favorite){
        Log.d("MyLog", "NoteViewModel: updateCategory start")
        viewModelScope.launch {
            val result = repository.updateFavorite(favorite)
            _insertResult.value = result
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: updateFavorite success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: updateFavorite error: ${result.exception.message}}")
                }
            }
        }
    }*/

    //Категории
    //Получение всех категорий
    fun getAllCategorys(): LiveData<List<Category>>{
        Log.d("MyLog", "NoteViewModel: getAllCategorys")
        return repository.getAllCategorys()
    }
    //Добовляет категорию
    fun insertCategory(category: Category){
        Log.d("MyLog", "NoteViewModel: insertCategory start")
        viewModelScope.launch {
            val result = repository.insertCategory(category)
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: insertCategory success")
                }
                is OperationResult.Error -> {
                    Log.e("MyLog", "NoteViewModel: insertCategory error: ${result.exception.message}}")
                }
            }
        }
    }
    //Функция для удаления категории
    fun deleteCategory(category: Category){
        Log.d("MyLog", "NoteViewModel: deleteCategory start")
        viewModelScope.launch {
            val result = repository.deleteCategory(category)
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: deleteCategory success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: deleteCategory error: ${result.exception.message}}")
                }
            }
        }
    }
    //Удаление категорий
    fun deleteCategorys(categorys: List<Category>){
        Log.d("MyLog", "NoteViewModel: deleteCategorys start")
        viewModelScope.launch{
            val categoryIds = categorys.map { it.id }
            val result = repository.deleteCategorys(categoryIds)
            _insertResult.value = result
            when (result) {
                is OperationResult.Success -> Log.d(
                    "MyLog",
                    "NoteViewModel: deleteCategorys success"
                )

                is OperationResult.Error -> Log.d(
                    "MyLog",
                    "NoteViewModel: deleteCategorys error: ${result.exception.message}"
                )
            }
        }
    }
    // Удаление всех категорий
    fun deleteAllCategorys(){
        Log.d("MyLog", "NoteViewModel: deleteAllCategorys start")
        viewModelScope.launch {
            val result = repository.deleteAllCategorys()
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> Log.d("MyLog", "NoteViewModel: deleteAllCategorys success")
                is OperationResult.Error -> Log.d("MyLog", "NoteViewModel: deleteAllCategorys error: ${result.exception.message}")
            }
        }
    }
    // Обновление категорий
    fun updateCategory(category: Category){
        Log.d("MyLog", "NoteViewModel: updateCategory start")
        viewModelScope.launch {
            val result = repository.updateCategory(category)
            _insertResult.value = result
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: updateCategory success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: updateCategory error: ${result.exception.message}}")
                }
            }
        }
    }

    //Заметки
    // Получение списка заметок через LiveData
    fun getAllNotes(): LiveData<List<Note>>{
        Log.d("MyLog", "NoteViewModel: getAllNotes")
        return repository.getAllNotes()
    }
    fun searchNotes(searchQuery: String): LiveData<List<Note>>{
        Log.d("MyLog", "NoteViewModel: searchNotes")
        return repository.searchNotes(searchQuery)
    }
    // Функция для добавления заметки
    fun insertNote(note: Note){
        Log.d("MyLog", "NoteViewModel: insertNote start")
        viewModelScope.launch {
           val result = repository.insertNote(note)
            _insertResult.value = result
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: insertNote success")
                }
                is OperationResult.Error -> {
                    Log.e("MyLog", "NoteViewModel: insertNote error: ${result.exception.message}}")
                }
            }
        }
    }
    // Функция для удаления заметки
    fun deleteNote(note: Note) {
        Log.d("MyLog", "NoteViewModel: deleteNote start")
        viewModelScope.launch {
            val result = repository.deleteNote(note)
            _insertResult.value = result
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: deleteNote success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: deleteNote error: ${result.exception.message}}")
                }
            }
        }
    }
    // Функция удаления заметок
    fun deleteNotes(notes: List<Note>) {
        Log.d("MyLog", "NoteViewModel: deleteNotes start")
        viewModelScope.launch {
            val noteIds = notes.map { it.id } // Список ID заметок
            val result = repository.deleteNotes(noteIds)
            _insertResult.value = result
            when (result) {
                is OperationResult.Success -> Log.d("MyLog", "NoteViewModel: deleteNotes success")
                is OperationResult.Error -> Log.d("MyLog", "NoteViewModel: deleteNotes error: ${result.exception.message}")
            }
        }
    }
    //Удаление всех заметок
    fun deleteAllNotesFromDB(){
        Log.d("MyLog", "NoteViewModel: deleteAllNotesFromDB start")
        viewModelScope.launch {
            val result = repository.deleteAllNotesFromDB()
            _insertResult.value = result
            when(result){
                is OperationResult.Success -> Log.d("MyLog", "NoteViewModel: deleteAllNotesFromDB success")
                is OperationResult.Error -> Log.d("MyLog", "NoteViewModel: deleteAllNotesFromDB error: ${result.exception.message}")
            }
        }
    }
    // Функция для обновления заметки
    fun updateNote(note: Note) {
        Log.d("MyLog", "NoteViewModel: updateNote start")
        viewModelScope.launch {
            val result = repository.updateNote(note)
            _insertResult.value = result
            when (result){
                is OperationResult.Success -> {
                    Log.d("MyLog", "NoteViewModel: updateNote success")
                }
                is OperationResult.Error -> {
                    Log.d("MyLog", "NoteViewModel: updateNote error: ${result.exception.message}}")
                }
            }
        }
    }
    // Получаем заметки в избранном
    fun getFavoriteListNote(): LiveData<List<Note>>{
        Log.d("MyLog", "NoteViewModel: getFavoriteListNote")
        return repository.getFavoriteListNote()
    }
}