package com.wisermit.hdrswitcher.util

import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.error
import com.wisermit.hdrswitcher.resources.invalid_file_dialog_message
import com.wisermit.hdrswitcher.resources.invalid_file_dialog_title
import com.wisermit.hdrswitcher.resources.unsupported_file_dialog_message
import com.wisermit.hdrswitcher.resources.unsupported_file_dialog_title
import org.jetbrains.compose.resources.getString
import javax.swing.JOptionPane

enum class DialogType(val jOptionPane: Int) {
    Error(JOptionPane.ERROR_MESSAGE),
    Default(JOptionPane.DEFAULT_OPTION);
}

object DialogUtils {

    fun showDialog(
        message: String,
        title: String? = null,
        type: DialogType = DialogType.Default,
    ) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            type.jOptionPane
        )
    }

    fun showErrorDialog(
        message: String,
        title: String? = null,
    ) {
        showDialog(
            message = message,
            title = title,
            type = DialogType.Error,
        )
    }

    suspend fun showErrorDialogFor(error: Throwable) {
        if (error is WiseError) {
            when (error) {
                is WiseError.InvalidFile -> showErrorDialog(
                    title = getString(Res.string.invalid_file_dialog_title),
                    message = getString(
                        Res.string.invalid_file_dialog_message,
                        error.fileName,
                        error.cause?.message.toString(),
                    ),
                )
                is WiseError.UnsupportedFile -> showErrorDialog(
                    title = getString(Res.string.unsupported_file_dialog_title),
                    message = getString(Res.string.unsupported_file_dialog_message, error.fileName),
                )
            }
        } else {
            showErrorDialog(
                title = getString(Res.string.error),
                message = "${error.javaClass.simpleName}: ${error.message}",
            )
        }
    }
}