package com.wisermit.hdrswitcher.utils

import java.io.File

class FileManager<T>(
    file: File,
    private val encode: (T) -> String,
    private val decode: (String) -> T,
    private val newData: () -> T,
    private val onDataChanged: (T) -> Unit,
) {

    val file: File by lazy {
        file.also {
            if (!it.exists()) {
                it.write(encode(newData()))
            }
        }
    }

    val data: T by lazy {
        val jsonString = file.readText()
        if (jsonString.isBlank()) {
            newData()
        } else {
            decode(jsonString)
        }.also {
            onDataChanged(it)
        }
    }

    fun write() = file.write(encode(data))

    private fun File.write(jsonString: String) {
        if (!exists()) parentFile.mkdirs()
        writeText(jsonString)
        onDataChanged(data)
    }
}