package com.wisermit.hdrswitcher.ui.components

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WindowsScrollBar(
    modifier: Modifier,
    content: @Composable BoxScope.(LazyListState) -> Unit
) {
    Box(modifier = modifier) {
        val listState = rememberLazyListState()

        val interactionSource = remember { MutableInteractionSource() }
        val isHovered = remember { mutableStateOf(false) }
        val thickness by animateDpAsState(
            targetValue = if (isHovered.value) 6.dp else 2.dp,
            animationSpec = spring(visibilityThreshold = Dp.VisibilityThreshold),
        )

        LaunchedEffect(this) {
            val hoverInteractions = mutableListOf<HoverInteraction.Enter>()
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hoverInteractions.add(interaction)
                    is HoverInteraction.Exit -> {
                        delay(500)
                        hoverInteractions.remove(interaction.enter)
                    }
                }
                isHovered.value = hoverInteractions.isNotEmpty()
            }
        }

        content(listState)

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 6.dp, end = 6.dp),
        ) {
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight(),
                interactionSource = interactionSource,
                adapter = rememberScrollbarAdapter(listState),
                style = LocalScrollbarStyle.current.copy(
                    thickness = thickness,
                    unhoverColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    hoverColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}