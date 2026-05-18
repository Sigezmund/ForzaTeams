package com.forza.teams.presentation.teams.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forza.teams.domain.repository.TeamsRepository
import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamsError
import com.forza.teams.presentation.teams.detail.SheetUi
import com.forza.teams.presentation.teams.detail.computeWinners
import com.forza.teams.presentation.teams.detail.toDetailUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MAX_SELECTION = 2

class TeamsListViewModel(private val repository: TeamsRepository) : ViewModel() {

    private val state = MutableStateFlow(TeamsListState())
    private val _refreshError = Channel<TeamsError>(Channel.BUFFERED)
    val refreshError = _refreshError.receiveAsFlow()

    val stateUi = state.map { state ->
        state.toUi()
    }.flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, TeamsListStateUi.Loading())

    val sheetUi = state.map { it.toSheetUi() }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, SheetUi.None)

    init {
        observeCache()
        loadTeams(isRefresh = false)
    }

    fun onRefresh() {
        loadTeams(isRefresh = true)
    }

    fun onRetry() {
        loadTeams(isRefresh = false)
    }

    fun onSortChange(field: SortField) {
        state.update { it.copy(sortField = field) }
        if (state.value.screenScene == ScreenScene.Error) {
            loadTeams(isRefresh = false)
        }
    }

    fun onSortOrderToggle() {
        state.update {
            it.copy(
                sortOrder = if (it.sortOrder == SortOrder.Ascending) {
                    SortOrder.Descending
                } else {
                    SortOrder.Ascending
                }
            )
        }
        if (state.value.screenScene == ScreenScene.Error) {
            loadTeams(isRefresh = false)
        }
    }

    fun onItemTap(id: String) {
        val current = state.value
        if (!current.isSelectionMode) {
            state.update { it.copy(selectedIds = setOf(id), isComparisonDialogVisible = true) }
        } else {
            val newIds = if (id in current.selectedIds) {
                current.selectedIds - id
            } else if (current.selectedIds.size < MAX_SELECTION) {
                current.selectedIds + id
            } else {
                current.selectedIds
            }
            state.update { it.copy(selectedIds = newIds, isComparisonDialogVisible = newIds.size == MAX_SELECTION) }
        }
    }

    fun onItemLongPress(id: String) {
        val current = state.value
        val newIds = when {
            id in current.selectedIds -> current.selectedIds - id
            current.selectedIds.size < MAX_SELECTION -> current.selectedIds + id
            else -> current.selectedIds
        }
        state.update { it.copy(selectedIds = newIds, isComparisonDialogVisible = newIds.size == MAX_SELECTION) }
    }

    fun onSheetDismissed() {
        state.update { it.copy(isComparisonDialogVisible = false, selectedIds = emptySet()) }
    }

    private fun observeCache() {
        viewModelScope.launch {
            repository.observeTeams().collect { teams ->
                state.update { current ->
                    val shouldShowCache = teams.isNotEmpty() &&
                            current.screenScene in setOf(ScreenScene.Loading, ScreenScene.Error)
                    current.copy(
                        teams = teams,
                        screenScene = if (shouldShowCache) ScreenScene.Loaded else current.screenScene,
                    )
                }
            }
        }
    }

    private fun loadTeams(isRefresh: Boolean = false) {
        viewModelScope.launch {
            state.update {
                it.copy(screenScene = if (isRefresh) ScreenScene.Refreshing else ScreenScene.Loading)
            }
            repository.refreshTeams()
                .onSuccess {
                    state.update { it.copy(screenScene = ScreenScene.Loaded) }
                }
                .onFailure { throwable ->
                    val error = throwable as? TeamsError ?: TeamsError.Unknown
                    if (state.value.teams.isNotEmpty()) {
                        state.update { it.copy(screenScene = ScreenScene.Loaded) }
                        if (isRefresh) _refreshError.trySend(error)
                    } else {
                        state.update { it.copy(screenScene = ScreenScene.Error, error = error) }
                    }
                }
        }
    }

    private fun TeamsListState.toUi(): TeamsListStateUi {
        return when (this.screenScene) {
            ScreenScene.Loading -> TeamsListStateUi.Loading(sortField, sortOrder)
            ScreenScene.Refreshing,
            ScreenScene.Loaded -> {
                TeamsListStateUi.Data(
                    isRefreshing = this.screenScene.isRefreshing,
                    teamList = this.teams.sorted(this.sortField, this.sortOrder)
                        .map {
                            it.toUi(
                                this.sortField,
                                isSelected = it.id in this.selectedIds && (this.selectedIds.size >= MAX_SELECTION || !this.isComparisonDialogVisible)
                            )
                        },
                    sortField = this.sortField,
                    sortOrder = this.sortOrder,
                    isSelectionMode = this.isSelectionMode,
                )
            }

            ScreenScene.Error -> TeamsListStateUi.Error(sortField, sortOrder, error.toSubtitleUi())
        }
    }

    private fun TeamsListState.toSheetUi(): SheetUi {
        if (!isComparisonDialogVisible) return SheetUi.None
        return when (selectedIds.size) {
            1 -> {
                val entity = teams.firstOrNull { it.id == selectedIds.first() } ?: return SheetUi.None
                SheetUi.Detail(entity.toDetailUi())
            }

            MAX_SELECTION -> {
                val (leftId, rightId) = selectedIds.toList()
                val left = teams.firstOrNull { it.id == leftId } ?: return SheetUi.None
                val right = teams.firstOrNull { it.id == rightId } ?: return SheetUi.None
                SheetUi.Comparison(
                    left = left.toDetailUi(),
                    right = right.toDetailUi(),
                    winners = computeWinners(left, right),
                )
            }

            else -> SheetUi.None
        }
    }

    private fun List<TeamEntity>.sorted(field: SortField, order: SortOrder): List<TeamEntity> {
        val ascending = when (field) {
            SortField.Name -> sortedBy { it.longName }
            SortField.Population -> sortedBy { it.population }
            SortField.WorldCupTitles -> sortedBy { it.worldCupTitles }
            SortField.RegisteredPlayers -> sortedBy { it.registeredPlayers }
            SortField.WcParticipations -> sortedBy { it.wcParticipations }
            SortField.WorldRank -> sortedBy { it.worldRank }
        }
        return if (order == SortOrder.Descending) ascending.reversed() else ascending
    }
}
