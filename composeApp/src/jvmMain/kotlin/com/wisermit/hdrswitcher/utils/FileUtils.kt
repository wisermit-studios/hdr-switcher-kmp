package com.wisermit.hdrswitcher.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path

object FileUtils {

    suspend fun getExeDescription(
        filePath: Path
    ): Result<String?> = withContext(Dispatchers.IO) {
        // FIXME: Fix charset (™).
        runCatching {
            val command = listOf(
                "powershell.exe",
                "-Command",
                "(Get-Item '$filePath').VersionInfo.FileDescription"
            )
            val process = ProcessBuilder(command).start()
            process.waitFor()
            InputStreamReader(process.inputStream)
                .buffered()
                .readLine()
        }
    }
}