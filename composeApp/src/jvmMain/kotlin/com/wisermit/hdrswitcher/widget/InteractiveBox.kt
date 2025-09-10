package com.wisermit.hdrswitcher.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ripple
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/*
 * Composable to properly apply the minimumInteractiveComponentSize,
 * which is not working as expected.
 * composeMultiplatform v1.8.2 / material3 v1.8.2
 */
@Composable
fun InteractiveBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
                onClickLabel = onClickLabel,
                role = role,
            )
            .minimumInteractiveComponentSize(),
    ) {
        Box(
            modifier = modifier.indication(interactionSource, ripple()),
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}