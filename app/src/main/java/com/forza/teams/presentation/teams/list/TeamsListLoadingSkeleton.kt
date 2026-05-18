package com.forza.teams.presentation.teams.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.forza.teams.ui.kit.SkeletonBox
import com.forza.teams.ui.theme.AppTheme

@Composable
fun TeamListLoading() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
    ) {
        items(12) {
            TeamItemSkeleton()
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = AppTheme.dimens.dp72),
                color = AppTheme.colors.dividerColor,
            )
        }
    }
}

@Composable
private fun TeamItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = AppTheme.dimens.dp8,
                horizontal = AppTheme.dimens.dp16,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SkeletonBox(
            modifier = Modifier
                .size(AppTheme.dimens.dp48)
                .padding(AppTheme.dimens.dp8),
            shape = AppTheme.shapes.circle,
        )
        Column(
            modifier = Modifier
                .padding(start = AppTheme.dimens.dp8)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.dp8),
        ) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(AppTheme.dimens.dp14),
            )
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(AppTheme.dimens.dp12),
            )
        }
    }
}
