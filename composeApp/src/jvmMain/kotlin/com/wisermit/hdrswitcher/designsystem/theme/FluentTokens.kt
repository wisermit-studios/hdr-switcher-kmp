@file:Suppress("unused")

package com.wisermit.hdrswitcher.designsystem.theme

import androidx.compose.ui.graphics.Color

internal object ColorTokens {
    val brand = Color(0xFF0067C0)
    val brandForeground = Color.White
    val secondary = Color(0xFFFBFBFB)
    val onSecondary = Color(0xFF1B1B1B)
    val error = Color(0xFFC42B1C)
    val layer1 = Color(0xFFF3F3F3)
    val layer2 = Color(0xFFF9F9F9)
    val layer3 = Color(0xFFFBFBFB)
    val layer4 = Color(0xFFFDFDFD)
    val layer5 = Color(0xFFFEFEFE)
    val dialogLayer = Color.White
    val layerForeground = Color(0xFF1B1B1B)
    val layerForegroundVariant = Color(0xFF606060)
    val outline1 = Color(0xFF8B8B8B)
    val outline2 = Color(0xFFB1B1B1)
    val outline3 = Color(0xFFE5E5E5)
    val outlineComboBox = Color(0xFFECECEC)
}

internal object ColorDarkTokens {
    val brand = Color(0xFF4CC2FF)
    val onBrand = Color(0xFF08151B)
    val secondary = Color(0xFF2D2D2D)
    val onSecondary = Color.White
    val error = Color(0xFFE81123)
    val layer1 = Color(0xFF202020)
    val layer2 = Color(0xFF272727)
    val layer3 = Color(0xFF2B2B2B)
    val layer4 = Color(0xFF323232)
    val layer5 = Color(0xFF373737)
    val dialogLayer = Color(0xFF262626)
    val layerForeground = Color.White
    val layerForegroundVariant = Color(0xFFD0D0D0)
    val outline1 = Color(0xFF9D9D9D)
    val outline2 = Color(0xFF3E3E3E)
    val outline3 = Color(0xFF1D1D1D)
    val outlineComboBox = Color(0xFF414141)
}

internal object StateTokens {
    const val DRAGGED_STATE_LAYER_OPACITY = 0.16f
    const val FOCUS_STATE_LAYER_OPACITY = 0.1f
    const val HOVER_STATE_LAYER_OPACITY = 0.02f
    const val PRESSED_STATE_LAYER_OPACITY = 0.08f
    const val DISABLED_STATE_LAYER_OPACITY = 0.38f
}