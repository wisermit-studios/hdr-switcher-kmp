package com.wisermit.hdrswitcher.utils

import com.wisermit.hdrswitcher.framework.AppError
import com.wisermit.hdrswitcher.framework.AppException
import hdrswitcher.composeapp.generated.resources.Res
import hdrswitcher.composeapp.generated.resources.error
import hdrswitcher.composeapp.generated.resources.error_message_unsupported_file
import org.jetbrains.compose.resources.getString
import javax.swing.JOptionPane

// TODO: Beautify error messages.
object ErrorUtils {

    suspend fun showError(message: String) {
        JOptionPane.showMessageDialog(
            null,
            message,
            getString(Res.string.error),
            JOptionPane.ERROR_MESSAGE
        )
    }

    suspend fun messageFor(throwable: Throwable): String {
        return if (throwable is AppException) {
            val error = throwable.error
            val stringRes = when (error) {
                is AppError.UnsupportedFile -> Res.string.error_message_unsupported_file
            }
            getString(stringRes, *error.args)
        } else {
            "${throwable.javaClass.simpleName}: ${throwable.message}"
        }
    }
}