package com.forza.teams.presentation.teams.list

import android.content.Context
import androidx.compose.runtime.Immutable
import com.forza.teams.R
import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamsError
import com.forza.teams.presentation.teams.detail.formatCount
import com.forza.teams.ui.kit.ImageUi
import com.forza.teams.ui.kit.TextUi

@Immutable
sealed interface TeamsListStateUi {
    val sortField: SortField
    val sortOrder: SortOrder

    data class Loading(override val sortField: SortField = SortField.Name, override val sortOrder: SortOrder = SortOrder.Ascending) : TeamsListStateUi
    data class Error(
        override val sortField: SortField,
        override val sortOrder: SortOrder,
        val subtitle: TextUi = TextUi.TextId(R.string.error_subtitle),
    ) : TeamsListStateUi

    data class Data(
        val teamList: List<TeamUi>,
        val isRefreshing: Boolean,
        override val sortField: SortField,
        override val sortOrder: SortOrder,
        val isSelectionMode: Boolean = false,
    ) : TeamsListStateUi
}

@Immutable
data class TeamUi(
    val id: String,
    val flag: ImageUi,
    val longName: TextUi,
    val sortValue: TextUi?,
    val isSelected: Boolean = false,
)

fun TeamEntity.toUi(sortField: SortField, isSelected: Boolean = false): TeamUi {
    val sortValue = when (sortField) {
        SortField.Name -> null
        SortField.Population -> population.formatCount()
        SortField.WorldCupTitles -> TextUi.Text(worldCupTitles.toString())
        SortField.RegisteredPlayers -> registeredPlayers.formatCount()
        SortField.WcParticipations -> TextUi.Text(wcParticipations.toString())
        SortField.WorldRank -> TextUi.Text(worldRank.toString())
    }
    return TeamUi(
        id = id,
        flag = ImageUi.ImageCustom(flag),
        longName = TextUi.Text(longName),
        sortValue = sortValue,
        isSelected = isSelected,
    )
}

fun SortField.toLabel(): TextUi = when (this) {
    SortField.Name -> TextUi.TextId(R.string.sort_field_name)
    SortField.Population -> TextUi.TextId(R.string.sort_field_population)
    SortField.WorldCupTitles -> TextUi.TextId(R.string.sort_field_world_cup_titles)
    SortField.RegisteredPlayers -> TextUi.TextId(R.string.sort_field_registered_players)
    SortField.WcParticipations -> TextUi.TextId(R.string.sort_field_wc_participations)
    SortField.WorldRank -> TextUi.TextId(R.string.sort_field_world_rank)
}

fun TeamsError?.toSubtitleUi(): TextUi = when (this) {
    TeamsError.NoConnection -> TextUi.TextId(R.string.error_subtitle_no_connection)
    TeamsError.Timeout -> TextUi.TextId(R.string.error_subtitle_timeout)
    is TeamsError.ServerError -> TextUi.TextId(R.string.error_subtitle_server)
    TeamsError.ParseError -> TextUi.TextId(R.string.error_subtitle_parse)
    TeamsError.Unknown, null -> TextUi.TextId(R.string.error_subtitle)
}

fun TeamsError.toRefreshMessage(context: Context): String = when (this) {
    TeamsError.NoConnection -> context.getString(R.string.error_refresh_no_connection)
    TeamsError.Timeout -> context.getString(R.string.error_refresh_timeout)
    else -> context.getString(R.string.error_refresh)
}
