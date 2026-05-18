package com.forza.teams.ui.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import com.forza.teams.ui.theme.AppTheme

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.backgroundColor,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        content = content,
    )
}
