package com.wisermit.hdrswitcher.core

sealed class WiseError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class InvalidFile(val fileName: String, cause: Throwable?) :
        WiseError(message = "Invalid $fileName", cause = cause)

    class UnsupportedFile(val fileName: String) : WiseError()
}

class ProcessException(
    message: String,
    commands: Array<out String>,
    causeMessage: String,
) : Exception(
    message +
            "\nCommand:\n    ${commands.joinToString("\n    ", "+ ")}" +
            "\nError: $causeMessage",
)