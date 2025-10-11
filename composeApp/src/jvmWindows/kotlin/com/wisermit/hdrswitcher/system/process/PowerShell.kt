package com.wisermit.hdrswitcher.system.process

import com.wisermit.hdrswitcher.framework.ProcessException

object PowerShell {

    @Throws(ProcessException::class)
    fun execute(
        vararg commands: String
    ): List<String> {
        return ProcessBuilder(
            listOf("powershell.exe", "-Command", *commands)
        ).start().run {
            val exitCode = waitFor()
            if (exitCode == 0) {
                inputReader().readLines()
            } else {
                val message = "PowerShell exited with code ${exitCode}."
                val error = errorReader().use { it.readText() }
                throw ProcessException(message, commands, error)
            }
        }
    }
}