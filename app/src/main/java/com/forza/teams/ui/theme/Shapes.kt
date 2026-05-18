package com.forza.teams.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

@Immutable
data class Shapes(
    val small: Shape = RoundedCornerShape(DimenTokens.Dp4),
    val medium: Shape = RoundedCornerShape(DimenTokens.Dp8),
    val large: Shape = RoundedCornerShape(DimenTokens.Dp16),
    val circle: Shape = CircleShape,
)

val LocalShapes = staticCompositionLocalOf { Shapes() }
