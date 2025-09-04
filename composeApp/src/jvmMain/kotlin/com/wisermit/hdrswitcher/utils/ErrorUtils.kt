package com.wisermit.hdrswitcher.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import javax.swing.JOptionPane

// TODO: Beautify error messages.
object ErrorUtils {

    fun showError(
        title: String,
        message: String,
    ) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        )
    }
}

@Composable
fun Flow<Throwable>.collectAsEvent() {
    LaunchedEffect(Unit) {

    }
}