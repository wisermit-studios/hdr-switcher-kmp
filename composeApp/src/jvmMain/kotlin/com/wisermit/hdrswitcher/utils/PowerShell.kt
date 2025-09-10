package com.wisermit.hdrswitcher.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

object PowerShell {

    suspend fun exec(
        vararg commands: String
    ): Result<List<String?>> = withContext(Dispatchers.IO) {
        runCatching {
            ProcessBuilder(
                listOf("powershell.exe", "-Command", *commands)
            ).start().run {
                val result = InputStreamReader(inputStream)
                    .buffered()
                    .readLines()

                val error = errorStream.bufferedReader().readText()

                val exitCode = waitFor().also { destroy() }

                if (exitCode == 0) {
                    result
                } else {
                    throw RuntimeException("PowerShell exited with code $exitCode: $error")
                        .also { it.printStackTrace() }
                }
            }
        }
    }
}