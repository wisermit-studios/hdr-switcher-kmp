package com.wisermit.hdrswitcher.framework

import org.jetbrains.compose.resources.StringResource

enum class AppError(
    val messageRes: StringResource? = null,
) {
    Unknown();
}

class AppException(error: AppError) : Exception("## ${error.name} error")

fun <T> AppError.toFailure(): Result<T> {
    val exception = AppException(this)
    println(exception)
    return exception.toFailure()
}

fun <T> Throwable.toFailure(): Result<T> {
    println(this)
    return Result.failure(this)
}
