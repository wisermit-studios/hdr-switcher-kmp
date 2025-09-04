package com.wisermit.hdrswitcher.framework

sealed class AppError(
    val param: String? = null,
) {
    class UnsupportedFile(fileName: String) : AppError(fileName)
}

class AppException(error: AppError) : Exception("## $error error")

fun <T> AppError.toFailure(): Result<T> {
    val exception = AppException(this)
    println(exception)
    return exception.toFailure()
}

fun <T> Throwable.toFailure(): Result<T> {
    println(this)
    return Result.failure(this)
}
