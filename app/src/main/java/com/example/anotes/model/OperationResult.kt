package com.example.anotes.model

sealed class OperationResult {
    object Success : OperationResult()
    data class Error(val exception: Throwable) : OperationResult()
}