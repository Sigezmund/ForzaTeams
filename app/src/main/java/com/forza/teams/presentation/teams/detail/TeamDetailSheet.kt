package com.forza.teams.presentation.teams.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.forza.teams.R
import com.forza.teams.ui.kit.ImageUi
import com.forza.teams.ui.kit.ImageUiCompose
import com.forza.teams.ui.kit.TextUi
import com.forza.teams.ui.kit.TextUiCompose
import com.forza.teams.ui.theme.AppTheme

private fun previewTeam(id: String, name: String, rank: String, pop: String, titles: String, players: String, wc: String) =
    TeamDetailUi(
        id = id,
        flag = ImageUi.ImageId(R.drawable.ic_team_placeholder),
        name = TextUi.Text(name),
        stats = mapOf(
            StatKey.WorldRank to StatRowUi(TextUi.TextId(R.string.sort_field_world_rank), TextUi.Text(rank)),
            StatKey.Population to StatRowUi(TextUi.TextId(R.string.sort_field_population), TextUi.Text(pop)),
            StatKey.WorldCupTitles to StatRowUi(TextUi.TextId(R.string.sort_field_world_cup_titles), TextUi.Text(titles)),
            StatKey.RegisteredPlayers to StatRowUi(TextUi.TextId(R.string.sort_field_registered_players), TextUi.Text(players)),
            StatKey.WcParticipations to StatRowUi(TextUi.TextId(R.string.sort_field_wc_participations), TextUi.Text(wc)),
        ),
    )

private val previewArgentina = previewTeam("55373", "Argentina", "1", "46M", "3", "1.5M", "18")
private val previewBrazil = previewTeam("4709", "Brazil", "4", "215M", "5", "5M", "22")

private val previewComparison = SheetUi.Comparison(
    left = previewArgentina,
    right = previewBrazil,
    winners = mapOf(
        StatKey.WorldRank to WinnerSide.Left,
        StatKey.Population to WinnerSide.Right,
        StatKey.WorldCupTitles to WinnerSide.Right,
        StatKey.RegisteredPlayers to WinnerSide.Right,
        StatKey.WcParticipations to WinnerSide.Right,
    ),
)

@Preview(name = "Detail Light", showBackground = true)
@Composable
private fun TeamSheetDetailLightPreview() {
    AppTheme(darkTheme = false) {
        TeamSheet(sheet = SheetUi.Detail(previewArgentina), onDismiss = {})
    }
}

@Preview(name = "Detail Dark", showBackground = true)
@Composable
private fun TeamSheetDetailDarkPreview() {
    AppTheme(darkTheme = true) {
        TeamSheet(sheet = SheetUi.Detail(previewArgentina), onDismiss = {})
    }
}

@Preview(name = "Comparison Light", showBackground = true)
@Composable
private fun TeamSheetComparisonLightPreview() {
    AppTheme(darkTheme = false) {
        TeamSheet(sheet = previewComparison, onDismiss = {})
    }
}

@Preview(name = "Comparison Dark", showBackground = true)
@Composable
private fun TeamSheetComparisonDarkPreview() {
    AppTheme(darkTheme = true) {
        TeamSheet(sheet = previewComparison, onDismiss = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamSheet(
    sheet: SheetUi.Active,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    when (sheet) {
                        is SheetUi.Detail -> TeamHeaderCell(team = sheet.team, modifier = Modifier.weight(1f))
                        is SheetUi.Comparison -> {
                            TeamHeaderCell(team = sheet.left, modifier = Modifier.weight(1f))
                            TeamHeaderCell(team = sheet.right, modifier = Modifier.weight(1f))
                        }
                    }
                }
                HorizontalDivider(color = AppTheme.colors.dividerColor)
            }
            items(StatKey.entries) { key ->
                StatRow(key = key, sheet = sheet)
                HorizontalDivider(color = AppTheme.colors.dividerColor)
            }
        }
    }
}

@Composable
private fun TeamHeaderCell(modifier: Modifier = Modifier, team: TeamDetailUi) {
    Column(
        modifier = modifier.padding(vertical = AppTheme.dimens.dp8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ImageUiCompose(
            modifier = Modifier
                .size(AppTheme.dimens.dp72)
                .padding(AppTheme.dimens.dp8),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_team_placeholder),
            image = team.flag,
        )
        TextUiCompose(
            text = team.name,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = AppTheme.dimens.dp8),
            style = AppTheme.typography.sheetTitle,
        )
    }
}

@Composable
private fun StatRow(key: StatKey, sheet: SheetUi.Active) {
    val winnerColor = AppTheme.colors.primaryColor
    val normalColor = AppTheme.colors.textColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.dp12, horizontal = AppTheme.dimens.dp16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (sheet) {
            is SheetUi.Detail -> {
                val stat = sheet.team.stats[key] ?: return
                TextUiCompose(text = stat.label, style = AppTheme.typography.statLabel)
                Spacer(Modifier.weight(1f))
                TextUiCompose(text = stat.value, style = AppTheme.typography.statValue)
            }

            is SheetUi.Comparison -> {
                val left = sheet.left.stats[key]
                val right = sheet.right.stats[key]
                val label = left?.label ?: right?.label ?: TextUi.Text("")
                val winner = sheet.winners[key] ?: WinnerSide.Tie
                TextUiCompose(
                    text = left?.value ?: TextUi.Text("-"),
                    color = if (winner == WinnerSide.Left) {
                        winnerColor
                    } else {
                        normalColor
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.statValue,
                )
                TextUiCompose(
                    text = label,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = AppTheme.dimens.dp4),
                    style = AppTheme.typography.statLabel,
                )
                TextUiCompose(
                    text = right?.value ?: TextUi.Text("-"),
                    color = if (winner == WinnerSide.Right) {
                        winnerColor
                    } else {
                        normalColor
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                    style = AppTheme.typography.statValue,
                )
            }
        }
    }
}
