package com.example.anotes.model

sealed class OperationResult {
    data class Success(val newId: Int) : OperationResult()
    data class Error(val exception: Throwable) : OperationResult()
}

//Пример обработки
/*
val result = repository.insertNote(note)
when (result) {
    is OperationResult.Success -> println("Операция прошла успешно")
    is OperationResult.Error -> println("Ошибка: ${result.exception.message}")
}*/
//Пример отправки результата
/*
return try{
    noteDao.delete(note)
    OperationResult.Success(-1) // Операция прошла успешно
}
catch (e: Exception){
    OperationResult.Error(e) // Возвращаем ошибку
}*/
