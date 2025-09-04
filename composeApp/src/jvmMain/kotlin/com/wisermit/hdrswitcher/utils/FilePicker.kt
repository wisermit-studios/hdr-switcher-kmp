package com.wisermit.hdrswitcher.utils

import com.wisermit.hdrswitcher.SystemInfo
import java.io.File
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileNameExtensionFilter

object FilePicker {

    val APPLICATION_FILTER = FileNameExtensionFilter("*.exe", "exe")

    fun show(
        onPick: (File) -> Unit,
        title: String? = null,
        fileFilter: FileFilter? = null,
        isAcceptAllFileFilterUsed: Boolean = false,
        currentDirectory: Path = SystemInfo.systemDrive,
    ) {
        val chooser = JFileChooser().also {
            it.dialogTitle = title
            it.fileFilter = fileFilter
            it.isAcceptAllFileFilterUsed = isAcceptAllFileFilterUsed
            it.currentDirectory = currentDirectory.toFile()
        }
        when (chooser.showOpenDialog(null)) {
            JFileChooser.APPROVE_OPTION -> onPick(chooser.selectedFile)
        }
    }
}