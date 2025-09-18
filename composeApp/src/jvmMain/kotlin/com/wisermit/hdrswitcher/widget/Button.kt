package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wisermit.hdrswitcher.ui.theme.ThemeDefaults
import androidx.compose.material3.Button as MaterialButton

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    MaterialButton(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .defaultMinSize(
                minHeight = ButtonDefaults.MinHeight,
            ),
        enabled = enabled,
        contentPadding = PaddingValues(
            horizontal = ButtonDefaults.HorizontalPadding,
        ),
        shape = shapes.extraSmall,
        onClick = onClick,
    ) {
        Text(
            text,
            modifier = Modifier.paddingFromBaseline(
                bottom = ThemeDefaults.TEXT_BASELINE_BOTTOM_FIX,
            ),
            fontWeight = FontWeight.Normal,
        )
    }
}

object ButtonDefaults {
    val MinHeight = 32.dp
    val HorizontalPadding = 24.dp
}