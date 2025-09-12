package com.wisermit.hdrswitcher.framework

sealed class AppError(
    message: String? = null,
    cause: Throwable? = null,
) : AppException(message, cause) {

    class InvalidFile(val fileName: String, cause: Throwable) : AppError(cause = cause)
    class UnsupportedFile(val fileName: String) : AppError()
}

class ProcessException(message: String) : AppException(message)

open class AppException(
    message: String?,
    cause: Throwable? = null,
) : Exception(message ?: cause?.message, cause)