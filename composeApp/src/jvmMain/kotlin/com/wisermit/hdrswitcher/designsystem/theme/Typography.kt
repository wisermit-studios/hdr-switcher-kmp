package com.wisermit.hdrswitcher.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun fluentTypography() = Typography(
    titleLarge = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.W500
    )
)