package com.forza.teams.ui.kit

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.forza.teams.ui.theme.AppTheme

@Composable
fun ButtonUiCompose(
    text: TextUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        TextUiCompose(
            text = text,
            color = AppTheme.colors.buttonTextColor,
        )
    }
}
