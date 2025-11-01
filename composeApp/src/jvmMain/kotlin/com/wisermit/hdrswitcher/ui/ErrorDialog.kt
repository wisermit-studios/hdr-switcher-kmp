package com.wisermit.hdrswitcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.wisermit.hdrswitcher.core.WiseError
import com.wisermit.hdrswitcher.designsystem.window.AlertDialogWindow
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.error
import com.wisermit.hdrswitcher.resources.invalid_file_dialog_message
import com.wisermit.hdrswitcher.resources.invalid_file_dialog_title
import com.wisermit.hdrswitcher.resources.unsupported_file_dialog_message
import com.wisermit.hdrswitcher.resources.unsupported_file_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorDialog(
    data: ErrorDialogData,
    onCloseRequest: () -> Unit,
) {
    AlertDialogWindow(
        text = data.text,
        title = data.title,
        onCloseRequest = onCloseRequest,
    )
}

@Immutable
data class ErrorDialogData(
    val text: String,
    val title: String? = null,
) {
    companion object Companion {

        @Composable
        fun from(error: Throwable): ErrorDialogData {
            return if (error is WiseError) {
                when (error) {
                    is WiseError.InvalidFile -> ErrorDialogData(
                        title = stringResource(Res.string.invalid_file_dialog_title),
                        text = stringResource(
                            Res.string.invalid_file_dialog_message,
                            error.fileName,
                            error.cause?.message.toString(),
                        ),
                    )

                    is WiseError.UnsupportedFile -> ErrorDialogData(
                        title = stringResource(Res.string.unsupported_file_dialog_title),
                        text = stringResource(
                            Res.string.unsupported_file_dialog_message,
                            error.fileName
                        ),
                    )
                }
            } else {
                ErrorDialogData(
                    title = stringResource(Res.string.error),
                    text = "${error::class.simpleName}: ${error.message}",
                )
            }
        }
    }
}