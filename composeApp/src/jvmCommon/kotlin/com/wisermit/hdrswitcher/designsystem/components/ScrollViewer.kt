package com.wisermit.hdrswitcher.designsystem.components

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScrollViewer(
    modifier: Modifier = Modifier,
    adapter: ScrollbarAdapter,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        content()

        ExpandableScrollbar(
            modifier = modifier.align(Alignment.CenterEnd),
            adapter = adapter,
        )
    }
}

@Composable
private fun ExpandableScrollbar(modifier: Modifier, adapter: ScrollbarAdapter) {
    val interactionSource = remember { MutableInteractionSource() }

    var thickness by remember { mutableStateOf(ScrollViewerDefaults.ThicknessDefault) }
    val animatedThickness by animateDpAsState(
        targetValue = thickness,
        animationSpec = spring(visibilityThreshold = Dp.VisibilityThreshold),
    )

    LaunchedEffect(Unit) {
        var delayedHoverExit: Job? = null

        interactionSource.interactions.collect {
            when (it) {
                is HoverInteraction.Enter -> {
                    delayedHoverExit?.cancel()
                    thickness = ScrollViewerDefaults.ThicknessHovered
                }
                is HoverInteraction.Exit -> {
                    delayedHoverExit = launch {
                        delay(ScrollViewerDefaults.SHRINK_DELAY)
                        thickness = ScrollViewerDefaults.ThicknessDefault
                    }
                }
            }
        }
    }

    VerticalScrollbar(
        modifier = modifier
            .padding(horizontal = ScrollViewerDefaults.MarginHorizontal)
            .alpha(ScrollViewerDefaults.OPACITY),
        style = LocalScrollbarStyle.current.copy(
            thickness = animatedThickness,
            unhoverColor = colorScheme.onSurface,
            hoverColor = colorScheme.onSurface,
        ),
        adapter = adapter,
        interactionSource = interactionSource,
    )
}

private object ScrollViewerDefaults {
    const val SHRINK_DELAY = 500L
    const val OPACITY = 0.5f

    val MarginHorizontal = 6.dp
    val ThicknessDefault = 2.dp
    val ThicknessHovered = 6.dp
}