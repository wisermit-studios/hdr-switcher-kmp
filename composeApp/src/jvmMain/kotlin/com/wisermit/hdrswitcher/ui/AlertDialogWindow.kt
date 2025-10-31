package com.wisermit.hdrswitcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.designsystem.components.Button
import com.wisermit.hdrswitcher.designsystem.components.SecondaryButton
import com.wisermit.hdrswitcher.designsystem.theme.FluentTheme
import com.wisermit.hdrswitcher.resources.Res
import com.wisermit.hdrswitcher.resources.cancel
import com.wisermit.hdrswitcher.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun AlertDialogWindow(
    onCloseRequest: () -> Unit,
    text: String,
    title: String? = null,
    confirmButtonText: String? = null,
    onConfirmRequest: (() -> Unit)? = null,
    cancelButtonText: String? = null,
    onCancelRequest: (() -> Unit)? = null,
) {
    FluentDialogWindow(onCloseRequest = onCloseRequest) {
        Column(
            Modifier
                .sizeIn(
                    minWidth = AlertDialogDefaults.MinWidth,
                    maxWidth = AlertDialogDefaults.MaxWidth,
                ),
        ) {
            Column(
                Modifier
                    .background(FluentTheme.colors.dialogSurface)
                    .fillMaxWidth()
                    .padding(AlertDialogDefaults.ContentPadding)
            ) {
                title?.let {
                    Text(
                        it,
                        modifier = Modifier.padding(AlertDialogDefaults.TitlePadding),
                        style = typography.titleLarge,
                    )
                }

                Text(text.normalizeLineBreaks(), style = typography.bodyMedium)
            }

            Row(
                Modifier.padding(AlertDialogDefaults.ButtonPanelPadding),
                horizontalArrangement = Arrangement.spacedBy(AlertDialogDefaults.ButtonsInnerSpace),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    text = confirmButtonText ?: stringResource(Res.string.ok),
                    onClick = onConfirmRequest ?: onCloseRequest,
                )

                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = cancelButtonText ?: stringResource(Res.string.cancel),
                    onClick = onCancelRequest ?: onCloseRequest,
                )
            }
        }
    }
}

// Workaround to fix line breaks with IntrinsicSize.
private fun String.normalizeLineBreaks() = replace("\n", " \n").plus(" ")

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

private object AlertDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 840.dp

    val ContentPadding = PaddingValues(16.dp)
    val TitlePadding = PaddingValues(bottom = 8.dp)

    val ButtonPanelPadding = PaddingValues(horizontal = 28.dp, vertical = 16.dp)
    val ButtonsInnerSpace = 8.dp
}