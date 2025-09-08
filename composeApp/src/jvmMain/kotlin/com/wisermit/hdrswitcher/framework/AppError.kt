package com.wisermit.hdrswitcher.framework

sealed class AppError(
    vararg val args: String = emptyArray(),
) {
    class UnsupportedFile(fileName: String) : AppError(fileName)
}

class AppException(val error: AppError) : Exception(error.javaClass.simpleName)

fun <T> AppError.toFailure(): Result<T> {
    val exception = AppException(this)
    return exception.toFailure()
}

fun <T> Throwable.toFailure(): Result<T> {
    println("## ${javaClass.simpleName}${message?.let { ": $it" }.orEmpty()}")
    return Result.failure(this)
}
