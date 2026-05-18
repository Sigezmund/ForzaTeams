package com.forza.teams.ui.kit

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import com.forza.teams.ui.theme.AppTheme

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.small,
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "skeleton-alpha",
    )
    Box(
        modifier = modifier
            .alpha(alpha)
            .clip(shape)
            .background(AppTheme.colors.skeletonColor),
    )
}
