package com.forza.teams.data.network

import com.forza.teams.domain.model.TeamEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkTeamsDataSource(private val api: TeamsApi) {
    suspend fun getTeams(): List<TeamEntity> = withContext(Dispatchers.IO) {
        api.getTeams().map { it.toEntity() }
    }
}
