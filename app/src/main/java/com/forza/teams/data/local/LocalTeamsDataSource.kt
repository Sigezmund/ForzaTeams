package com.forza.teams.data.local

import com.forza.teams.domain.model.TeamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalTeamsDataSource(private val dao: TeamsDao) {

    fun observeTeams(): Flow<List<TeamEntity>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun saveTeams(teams: List<TeamEntity>) =
        dao.replaceAll(teams.map { it.toRoomEntity() })
}
