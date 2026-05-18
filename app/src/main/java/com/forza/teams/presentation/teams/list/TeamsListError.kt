package com.forza.teams.presentation.teams.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.forza.teams.R
import com.forza.teams.ui.kit.ButtonUiCompose
import com.forza.teams.ui.kit.ImageUi
import com.forza.teams.ui.kit.ImageUiCompose
import com.forza.teams.ui.kit.ScreenContainer
import com.forza.teams.ui.kit.TextUi
import com.forza.teams.ui.kit.TextUiCompose
import com.forza.teams.ui.kit.toTextUi
import com.forza.teams.ui.theme.AppTheme
import com.forza.teams.ui.theme.ColorTokens

@Preview(name = "Error Light", showBackground = true)
@Composable
private fun TeamListErrorPreviewLight() {
    AppTheme(darkTheme = false) {
        ScreenContainer {
            TeamListError()
        }
    }
}

@Preview(name = "Error Dark", showBackground = true)
@Composable
private fun TeamListErrorPreviewDark() {
    AppTheme(darkTheme = true) {
        ScreenContainer {
            TeamListError()
        }
    }
}

@Composable
fun TeamListError(
    subtitle: TextUi = R.string.error_subtitle.toTextUi(),
    onRetry: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = AppTheme.dimens.dp16),
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.dp72)
                    .background(ColorTokens.colorError, AppTheme.shapes.circle),
                contentAlignment = Alignment.Center,
            ) {
                ImageUiCompose(
                    image = ImageUi.ImageId(R.drawable.ic_warning),
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(AppTheme.dimens.dp40),
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.dp16))
            TextUiCompose(text = R.string.error_title.toTextUi())
            Spacer(modifier = Modifier.height(AppTheme.dimens.dp8))
            TextUiCompose(
                text = subtitle,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.dp16))
            ButtonUiCompose(
                text = R.string.error_retry.toTextUi(),
                onClick = onRetry,
            )
        }
    }
}
