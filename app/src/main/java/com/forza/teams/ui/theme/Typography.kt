package com.forza.teams.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Immutable
data class Typography(
    val teamName: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = TypographyTokens.fontSizeMd,
        lineHeight = TypographyTokens.lineHeightMd,
    ),
    val sortValue: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = TypographyTokens.fontSizeSm,
        lineHeight = TypographyTokens.lineHeightSm,
    ),
    val sheetTitle: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = TypographyTokens.fontSizeLg,
        lineHeight = TypographyTokens.lineHeightLg,
    ),
    val statLabel: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = TypographyTokens.fontSizeSm,
        lineHeight = TypographyTokens.lineHeightSm,
    ),
    val statValue: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = TypographyTokens.fontSizeSm,
        lineHeight = TypographyTokens.lineHeightSm,
    ),
)

val LocalTypography = staticCompositionLocalOf { Typography() }
