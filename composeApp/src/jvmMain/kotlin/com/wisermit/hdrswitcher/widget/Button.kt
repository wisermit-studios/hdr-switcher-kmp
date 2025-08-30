package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.Theme
import androidx.compose.material3.Button as Material3Button

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Material3Button(
        onClick = onClick,
        modifier = modifier.height(Theme.BUTTON_HEIGHT),
        contentPadding = PaddingValues(horizontal = 24.dp),
        enabled = enabled,
        shape = shapes.extraSmall,
        content = {
            Text(
                text,
                modifier = Modifier.paddingFromBaseline(bottom = Theme.TEXT_BASELINE_PADDING),
                fontWeight = FontWeight.Normal,
            )
        }
    )
}