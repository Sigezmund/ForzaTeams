package com.forza.teams.domain.repository

import com.forza.teams.domain.model.TeamEntity
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    fun observeTeams(): Flow<List<TeamEntity>>
    suspend fun refreshTeams(): Result<Unit>
}
