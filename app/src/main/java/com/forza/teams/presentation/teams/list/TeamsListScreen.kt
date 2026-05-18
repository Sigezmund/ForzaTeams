package com.forza.teams.presentation.teams.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forza.teams.R
import com.forza.teams.presentation.teams.detail.SheetUi
import com.forza.teams.presentation.teams.detail.TeamSheet
import com.forza.teams.ui.kit.ImageUi
import com.forza.teams.ui.kit.ImageUiCompose
import com.forza.teams.ui.kit.ScreenContainer
import com.forza.teams.ui.kit.SortDropdown
import com.forza.teams.ui.kit.TextUi
import com.forza.teams.ui.kit.TextUiCompose
import com.forza.teams.ui.kit.toTextUi
import com.forza.teams.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

private val previewState = TeamsListStateUi.Data(
    teamList = listOf(
        TeamUi("35010", flag = ImageUi.ImageId(R.drawable.flag_algeria), longName = TextUi.Text("Algeria"), sortValue = TextUi.Text("36")),
        TeamUi("55373", flag = ImageUi.ImageId(R.drawable.flag_argentina), longName = TextUi.Text("Argentina"), sortValue = TextUi.Text("1")),
        TeamUi("3130", flag = ImageUi.ImageId(R.drawable.flag_australia), longName = TextUi.Text("Australia"), sortValue = TextUi.Text("24")),
        TeamUi("21967", flag = ImageUi.ImageId(R.drawable.flag_austria), longName = TextUi.Text("Austria"), sortValue = TextUi.Text("21")),
        TeamUi("8902", flag = ImageUi.ImageId(R.drawable.flag_belgium), longName = TextUi.Text("Belgium"), sortValue = TextUi.Text("8")),
        TeamUi(
            "99901",
            flag = ImageUi.ImageId(R.drawable.ic_team_placeholder),
            longName = TextUi.Text("Bosnia and Herzegovina"),
            sortValue = TextUi.Text("58")
        ),
        TeamUi("4709", flag = ImageUi.ImageId(R.drawable.flag_brazil), longName = TextUi.Text("Brazil"), sortValue = TextUi.Text("4")),
        TeamUi("9148", flag = ImageUi.ImageId(R.drawable.flag_cabo_verde), longName = TextUi.Text("Cabo Verde"), sortValue = TextUi.Text("55")),
        TeamUi("27123", flag = ImageUi.ImageId(R.drawable.flag_canada), longName = TextUi.Text("Canada"), sortValue = TextUi.Text("41")),
        TeamUi("28428", flag = ImageUi.ImageId(R.drawable.ic_team_placeholder), longName = TextUi.Text("Colombia"), sortValue = TextUi.Text("12")),
    ),
    isRefreshing = false,
    sortField = SortField.Name,
    sortOrder = SortOrder.Descending,
)

@Preview(name = "Light", showBackground = true)
@Composable
private fun TeamsListPreviewLight() {
    AppTheme(darkTheme = false) {
        ScreenContainer {
            TeamsList(previewState)
        }
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun TeamsListPreviewDark() {
    AppTheme(darkTheme = true) {
        ScreenContainer {
            TeamsList(previewState)
        }
    }
}

@Preview(name = "Loading Light", showBackground = true)
@Composable
private fun TeamsListLoadingPreviewLight() {
    AppTheme(darkTheme = false) {
        ScreenContainer {
            TeamsList(TeamsListStateUi.Loading(SortField.WorldRank, SortOrder.Descending))
        }
    }
}

@Preview(name = "Loading Dark", showBackground = true)
@Composable
private fun TeamsListLoadingPreviewDark() {
    AppTheme(darkTheme = true) {
        ScreenContainer {
            TeamsList(TeamsListStateUi.Loading(SortField.WorldRank, SortOrder.Descending))
        }
    }
}

@Preview(name = "Error Light", showBackground = true)
@Composable
private fun TeamsListErrorPreviewLight() {
    AppTheme(darkTheme = false) {
        ScreenContainer {
            TeamsList(TeamsListStateUi.Error(SortField.WorldRank, SortOrder.Descending))
        }
    }
}

@Preview(name = "Error Dark", showBackground = true)
@Composable
private fun TeamsListErrorPreviewDark() {
    AppTheme(darkTheme = true) {
        ScreenContainer {
            TeamsList(TeamsListStateUi.Error(SortField.WorldRank, SortOrder.Descending))
        }
    }
}

@Composable
fun TeamsListScreen(
    viewModel: TeamsListViewModel = koinViewModel()
) {
    val state by viewModel.stateUi.collectAsStateWithLifecycle()
    val sheetUi by viewModel.sheetUi.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refreshError.collect { error ->
            snackbarHostState.showSnackbar(error.toRefreshMessage(context))
        }
    }

    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val backgroundColor = AppTheme.colors.backgroundColor
    val gradientBrush = remember(backgroundColor) {
        Brush.verticalGradient(listOf(Color.Transparent, backgroundColor))
    }

    ScreenContainer {
        TeamsList(
            state = state,
            onRefresh = { viewModel.onRefresh() },
            onRetry = { viewModel.onRetry() },
            onSortChange = { viewModel.onSortChange(it) },
            onSortOrderToggle = { viewModel.onSortOrderToggle() },
            onItemTap = { viewModel.onItemTap(it) },
            onItemLongPress = { viewModel.onItemLongPress(it) },
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding + AppTheme.dimens.dp48)
                .align(Alignment.BottomCenter)
                .background(gradientBrush)
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }

    when (val sheet = sheetUi) {
        is SheetUi.Active -> TeamSheet(
            sheet = sheet,
            onDismiss = { viewModel.onSheetDismissed() },
        )

        SheetUi.None -> Unit
    }
}

@Composable
private fun TeamsList(
    state: TeamsListStateUi,
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onSortChange: (SortField) -> Unit = {},
    onSortOrderToggle: () -> Unit = {},
    onItemTap: (id: String) -> Unit = {},
    onItemLongPress: (id: String) -> Unit = {},
) {
    TeamsListLayout(state.sortField, state.sortOrder, onSortChange, onSortOrderToggle) {
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            contentKey = { it::class },
            label = "content",
        ) { animatedState ->
            when (animatedState) {
                is TeamsListStateUi.Data -> TeamListData(animatedState, onRefresh, onItemTap, onItemLongPress)
                is TeamsListStateUi.Loading -> TeamListLoading()
                is TeamsListStateUi.Error -> TeamListError(subtitle = animatedState.subtitle, onRetry = onRetry)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamsListLayout(
    sortField: SortField,
    sortOrder: SortOrder,
    onSortChange: (SortField) -> Unit,
    onSortOrderToggle: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                TextUiCompose(R.string.app_name.toTextUi())
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.dp16, vertical = AppTheme.dimens.dp8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SortDropdown(
                modifier = Modifier.weight(1f),
                selected = sortField,
                options = SortField.entries,
                labelOf = SortField::toLabel,
                onSelect = onSortChange,
                title = R.string.sort_label.toTextUi(),
            )
            IconButton(onClick = onSortOrderToggle) {
                Crossfade(
                    targetState = sortOrder,
                    animationSpec = tween(durationMillis = 300),
                ) { order ->
                    ImageUiCompose(
                        image = ImageUi.ImageId(
                            if (order == SortOrder.Ascending) {
                                R.drawable.ic_sort_asc
                            } else {
                                R.drawable.ic_sort_desc
                            }
                        ),
                        colorFilter = ColorFilter.tint(AppTheme.colors.textColor),
                        withCrossFade = false,
                    )
                }
            }
        }
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamListData(
    state: TeamsListStateUi.Data,
    onRefresh: () -> Unit,
    onItemTap: (id: String) -> Unit = {},
    onItemLongPress: (id: String) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.sortField) {
        listState.scrollToItem(0)
    }
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            items(state.teamList, key = { it.id }) { teamUi ->
                TeamItem(teamUi, onItemTap, onItemLongPress)
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = AppTheme.dimens.dp72),
                    color = AppTheme.colors.dividerColor
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeamItem(
    team: TeamUi,
    onItemTap: (id: String) -> Unit,
    onItemLongPress: (id: String) -> Unit,
) {
    val rowBackground by animateColorAsState(
        targetValue = if (team.isSelected) AppTheme.colors.primary15 else Color.Transparent,
        animationSpec = tween(200),
        label = "row-bg",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onItemTap(team.id) },
                onLongClick = { onItemLongPress(team.id) },
            )
            .background(rowBackground)
            .padding(
                vertical = AppTheme.dimens.dp8,
                horizontal = AppTheme.dimens.dp16
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(AppTheme.dimens.dp48),
            contentAlignment = Alignment.Center,
        ) {
            val selectionAlpha by animateFloatAsState(
                targetValue = if (team.isSelected) 1f else 0f,
                animationSpec = tween(200),
                label = "selection",
            )
            ImageUiCompose(
                modifier = Modifier
                    .size(AppTheme.dimens.dp48)
                    .padding(AppTheme.dimens.dp8)
                    .alpha(1f - selectionAlpha),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_team_placeholder),
                image = team.flag,
            )
            SelectionCircle(modifier = Modifier.alpha(selectionAlpha))
        }
        Column(
            modifier = Modifier
                .padding(start = AppTheme.dimens.dp8)
                .weight(1f)
        ) {
            TextUiCompose(text = team.longName, style = AppTheme.typography.teamName)
            team.sortValue?.let { TextUiCompose(text = it, style = AppTheme.typography.sortValue) }
        }
    }
}

@Composable
private fun SelectionCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(AppTheme.dimens.dp32)
            .background(AppTheme.colors.primaryColor, AppTheme.shapes.circle),
        contentAlignment = Alignment.Center,
    ) {
        ImageUiCompose(
            image = ImageUi.ImageId(R.drawable.ic_checkmark),
            colorFilter = ColorFilter.tint(AppTheme.colors.buttonTextColor),
        )
    }
}
