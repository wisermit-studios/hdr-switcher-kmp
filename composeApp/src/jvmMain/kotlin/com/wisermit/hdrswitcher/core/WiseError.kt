package com.wisermit.hdrswitcher.core

sealed class WiseError(
    message: String? = null,
    cause: Throwable? = null,
) : WiseException(message, cause = cause) {

    class InvalidFile(val fileName: String, cause: Throwable) : WiseError(cause = cause)
    class UnsupportedFile(val fileName: String) : WiseError()
}

open class WiseException(
    message: String?,
    val detailedMessage: String? = message,
    cause: Throwable? = null,
) : Exception(message ?: cause?.message, cause)

class ProcessException(
    message: String,
    commands: Array<out String>,
    causeMessage: String,
) : WiseException(
    message,
    detailedMessage = message +
            "\nCommand:\n    ${commands.joinToString("\n    ", "+ ")}" +
            "\nError: $causeMessage"
)