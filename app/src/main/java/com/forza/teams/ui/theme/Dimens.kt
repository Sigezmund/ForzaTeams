package com.forza.teams.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

@Immutable
data class Dimens(
    val dp2: Dp = DimenTokens.Dp2,
    val dp4: Dp = DimenTokens.Dp4,
    val dp6: Dp = DimenTokens.Dp6,
    val dp8: Dp = DimenTokens.Dp8,
    val dp10: Dp = DimenTokens.Dp10,
    val dp12: Dp = DimenTokens.Dp12,
    val dp14: Dp = DimenTokens.Dp14,
    val dp16: Dp = DimenTokens.Dp16,
    val dp20: Dp = DimenTokens.Dp20,
    val dp24: Dp = DimenTokens.Dp24,
    val dp28: Dp = DimenTokens.Dp28,
    val dp32: Dp = DimenTokens.Dp32,
    val dp40: Dp = DimenTokens.Dp40,
    val dp48: Dp = DimenTokens.Dp48,
    val dp56: Dp = DimenTokens.Dp56,
    val dp72: Dp = DimenTokens.Dp72,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }