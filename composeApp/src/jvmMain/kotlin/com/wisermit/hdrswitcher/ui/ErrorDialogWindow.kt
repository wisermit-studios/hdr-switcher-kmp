package com.wisermit.hdrswitcher.ui

import androidx.compose.runtime.Composable
import com.wisermit.hdrswitcher.designsystem.AlertDialogWindow

@Composable
fun ErrorDialogWindow(
    data: UiErrorData,
    onCloseRequest: () -> Unit,
) {
    AlertDialogWindow(
        text = data.text,
        title = data.title,
        onCloseRequest = onCloseRequest,
    )
}