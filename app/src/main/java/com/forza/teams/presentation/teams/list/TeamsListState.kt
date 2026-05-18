package com.forza.teams.presentation.teams.list

import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamsError

data class TeamsListState(
    val teams: List<TeamEntity> = emptyList(),
    val screenScene: ScreenScene = ScreenScene.Loading,
    val sortField: SortField = SortField.Name,
    val sortOrder: SortOrder = SortOrder.Ascending,
    val selectedIds: Set<String> = emptySet(),
    val isComparisonDialogVisible: Boolean = false,
    val error: TeamsError? = null,
)

val TeamsListState.isSelectionMode: Boolean get() = selectedIds.isNotEmpty() && !isComparisonDialogVisible


enum class ScreenScene {
    Loading,
    Refreshing,
    Loaded,
    Error
}

val ScreenScene.isRefreshing: Boolean get() = this == ScreenScene.Refreshing

enum class SortOrder { Ascending, Descending }

enum class SortField {
    Name,
    Population,
    WorldCupTitles,
    RegisteredPlayers,
    WcParticipations,
    WorldRank,
}
