package com.wisermit.hdrswitcher.framework

sealed class AppError {
    class InvalidFile(val fileName: String) : AppError()
    class UnsupportedFile(val fileName: String) : AppError()
}

class AppException(
    val error: AppError,
    throwable: Throwable? = null,
) : Exception(throwable?.message ?: error.javaClass.simpleName, throwable)

fun <T> AppError.toFailure(): Result<T> {
    val exception = AppException(this)
    return exception.toFailure()
}

fun <T> Throwable.toFailure(): Result<T> {
    println("## ${javaClass.simpleName}${message?.let { ": $it" }.orEmpty()}")
    return Result.failure(this)
}