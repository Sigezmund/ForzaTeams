package com.forza.teams.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.material3.darkColorScheme as materialDarkColorScheme
import androidx.compose.material3.lightColorScheme as materialLightColorScheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = remember(darkTheme) { if (darkTheme) darkColorScheme() else lightColorScheme() }
    val materialColorScheme = remember(colorScheme) { colorScheme.toMaterial(darkTheme) }
    val rippleConfig = remember(colorScheme.rippleColor) { RippleConfiguration(color = colorScheme.rippleColor) }
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalRippleConfiguration provides rippleConfig,
    ) {
        MaterialTheme(colorScheme = materialColorScheme) {
            content()
        }
    }
}

private fun ColorScheme.toMaterial(darkTheme: Boolean): androidx.compose.material3.ColorScheme {
    val base = if (darkTheme) materialDarkColorScheme() else materialLightColorScheme()
    return base.copy(
        primary = buttonColor,
        onPrimary = buttonTextColor,
        background = backgroundColor,
        onBackground = textColor,
        surface = backgroundColor,
        onSurface = textColor,
        surfaceVariant = backgroundColor,
        onSurfaceVariant = textColor,
        surfaceContainer = backgroundColor,
        surfaceContainerLow = backgroundColor,
        surfaceContainerLowest = backgroundColor,
        surfaceContainerHigh = backgroundColor,
        surfaceContainerHighest = backgroundColor,
        surfaceBright = backgroundColor,
        surfaceDim = backgroundColor,
        outline = dividerColor,
        outlineVariant = dividerColor,
    )
}


object AppTheme {

    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val dimens: Dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

}
