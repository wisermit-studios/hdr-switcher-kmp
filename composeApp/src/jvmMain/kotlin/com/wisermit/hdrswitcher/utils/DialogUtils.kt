package com.wisermit.hdrswitcher.utils

import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.framework.AppException
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.error
import hdrswitcher.composeapp.generated.resources.invalid_file_dialog_message
import hdrswitcher.composeapp.generated.resources.invalid_file_dialog_title
import hdrswitcher.composeapp.generated.resources.unsupported_file_dialog_message
import hdrswitcher.composeapp.generated.resources.unsupported_file_dialog_title
import org.jetbrains.compose.resources.getString
import javax.swing.JOptionPane

enum class DialogType(val jOptionPane: Int) {
    Error(JOptionPane.ERROR_MESSAGE),
    Default(JOptionPane.DEFAULT_OPTION);
}

// TODO: Beautify error messages.
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

    suspend fun showErrorDialogFor(throwable: Throwable) {
        if (throwable is AppException) {
            val error = throwable.error
            when (error) {
                is AppError.InvalidFile -> showErrorDialog(
                    title = getString(Res.string.invalid_file_dialog_title),
                    message = getString(
                        Res.string.invalid_file_dialog_message,
                        error.fileName,
                        throwable.message.toString(),
                        ),
                )
                is AppError.UnsupportedFile -> showErrorDialog(
                    title = getString(Res.string.unsupported_file_dialog_title),
                    message = getString(Res.string.unsupported_file_dialog_message, error.fileName),
                )
            }
        } else {
            showErrorDialog(
                title = getString(Res.string.error),
                message = "${throwable.javaClass.simpleName}: ${throwable.message}",
            )
        }
    }
}