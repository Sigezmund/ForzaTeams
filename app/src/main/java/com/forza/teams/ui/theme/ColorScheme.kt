package com.forza.teams.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorScheme(
    val primaryColor: Color,
    val primary15: Color,
    val rippleColor: Color,
    val textColor: Color,
    val dividerColor: Color,
    val backgroundColor: Color,
    val skeletonColor: Color,
    val buttonColor: Color,
    val buttonTextColor: Color,
)

fun lightColorScheme() = ColorScheme(
    primaryColor = ColorTokens.colorDarkGreen,
    primary15 = ColorTokens.colorDarkGreenTint15,
    rippleColor = ColorTokens.colorRippleLight,
    textColor = Color.Black,
    dividerColor = ColorTokens.colorLightDivider,
    backgroundColor = ColorTokens.colorLightBackground,
    skeletonColor = ColorTokens.colorLightSkeleton,
    buttonColor = ColorTokens.colorButtonLight,
    buttonTextColor = ColorTokens.colorButtonTextLight,
)

fun darkColorScheme() = ColorScheme(
    primaryColor = ColorTokens.colorLightGreen,
    primary15 = ColorTokens.colorLightGreenTint15,
    rippleColor = ColorTokens.colorRippleDark,
    textColor = Color.White,
    dividerColor = ColorTokens.colorDarkDivider,
    backgroundColor = ColorTokens.colorDarkBackground,
    skeletonColor = ColorTokens.colorDarkSkeleton,
    buttonColor = ColorTokens.colorButtonDark,
    buttonTextColor = ColorTokens.colorButtonTextDark,
)

val LocalColorScheme = staticCompositionLocalOf { lightColorScheme() }
