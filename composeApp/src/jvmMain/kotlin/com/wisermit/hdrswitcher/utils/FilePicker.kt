package com.wisermit.hdrswitcher.utils

import com.wisermit.hdrswitcher.SystemInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileNameExtensionFilter

object FilePicker : KoinComponent {

    private val systemInfo: SystemInfo by inject()

    val applicationFilter
        get() = FileNameExtensionFilter(
            "*.${systemInfo.applicationExtension}",
            systemInfo.applicationExtension,
        )

    fun show(
        onPick: (File) -> Unit,
        title: String? = null,
        fileFilter: FileFilter? = null,
        isAcceptAllFileFilterUsed: Boolean = false,
        currentDirectory: Path = systemInfo.systemDrive,
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