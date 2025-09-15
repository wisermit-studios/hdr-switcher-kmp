package com.wisermit.hdrswitcher.infrastructure

import com.wisermit.hdrswitcher.framework.ProcessException

object PowerShell {

    @Throws(ProcessException::class)
    fun exec(
        vararg commands: String
    ): List<String> {
        return ProcessBuilder(
            listOf("powershell.exe", "-Command", *commands)
        ).start().run {
            val result = inputStream.reader().use { it.buffered().readLines() }
            val error = errorStream.reader().use { it.buffered().readText() }

            val exitCode = waitFor()
            destroy()

            if (exitCode == 0) {
                result
            } else {
                val errorMessage = "PowerShell exited with code $exitCode." +
                        "\nCommand: $commands" +
                        "\nError: $error"
                throw ProcessException(errorMessage)
            }
        }
    }
}