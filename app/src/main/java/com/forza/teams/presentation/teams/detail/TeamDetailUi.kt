package com.forza.teams.presentation.teams.detail

import androidx.compose.runtime.Immutable
import com.forza.teams.R
import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.ui.kit.ImageUi
import com.forza.teams.ui.kit.TextUi

enum class StatKey { WorldRank, Population, WorldCupTitles, RegisteredPlayers, WcParticipations }

@Immutable
data class StatRowUi(val label: TextUi, val value: TextUi)

@Immutable
data class TeamDetailUi(val id: String, val flag: ImageUi, val name: TextUi, val stats: Map<StatKey, StatRowUi>)

enum class WinnerSide { Left, Right, Tie }

@Immutable
sealed interface SheetUi {
    object None : SheetUi

    sealed interface Active : SheetUi

    @Immutable
    data class Detail(val team: TeamDetailUi) : Active

    @Immutable
    data class Comparison(
        val left: TeamDetailUi,
        val right: TeamDetailUi,
        val winners: Map<StatKey, WinnerSide>,
    ) : Active
}

fun Int.formatCount(): TextUi = when {
    this >= 1_000_000 -> TextUi.TextResFormatted(R.string.population_millions, listOf(this / 1_000_000))
    else -> TextUi.TextResFormatted(R.string.population_thousands, listOf(this / 1_000))
}

fun TeamEntity.toDetailUi() = TeamDetailUi(
    id = id,
    flag = ImageUi.ImageCustom(flag),
    name = TextUi.Text(longName),
    stats = mapOf(
        StatKey.WorldRank to StatRowUi(TextUi.TextId(R.string.sort_field_world_rank), TextUi.Text(worldRank.toString())),
        StatKey.Population to StatRowUi(TextUi.TextId(R.string.sort_field_population), population.formatCount()),
        StatKey.WorldCupTitles to StatRowUi(TextUi.TextId(R.string.sort_field_world_cup_titles), TextUi.Text(worldCupTitles.toString())),
        StatKey.RegisteredPlayers to StatRowUi(TextUi.TextId(R.string.sort_field_registered_players), registeredPlayers.formatCount()),
        StatKey.WcParticipations to StatRowUi(TextUi.TextId(R.string.sort_field_wc_participations), TextUi.Text(wcParticipations.toString())),
    )
)

fun computeWinners(left: TeamEntity, right: TeamEntity): Map<StatKey, WinnerSide> {
    fun cmp(l: Int, r: Int, lowerIsBetter: Boolean) = when {
        l == r -> WinnerSide.Tie
        (lowerIsBetter && l < r) || (!lowerIsBetter && l > r) -> WinnerSide.Left
        else -> WinnerSide.Right
    }
    return mapOf(
        StatKey.WorldRank to cmp(left.worldRank, right.worldRank, lowerIsBetter = true),
        StatKey.Population to cmp(left.population, right.population, lowerIsBetter = false),
        StatKey.WorldCupTitles to cmp(left.worldCupTitles, right.worldCupTitles, lowerIsBetter = false),
        StatKey.RegisteredPlayers to cmp(left.registeredPlayers, right.registeredPlayers, lowerIsBetter = false),
        StatKey.WcParticipations to cmp(left.wcParticipations, right.wcParticipations, lowerIsBetter = false),
    )
}
