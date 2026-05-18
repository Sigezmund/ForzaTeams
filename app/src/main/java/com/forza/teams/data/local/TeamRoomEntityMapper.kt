package com.forza.teams.data.local

import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamFlagEntity

fun TeamRoomEntity.toDomain(): TeamEntity = TeamEntity(
    id = id,
    flag = TeamFlagEntity(teamId = id),
    longName = longName,
    population = population,
    worldCupTitles = worldCupTitles,
    registeredPlayers = registeredPlayers,
    wcParticipations = wcParticipations,
    worldRank = worldRank,
)

fun TeamEntity.toRoomEntity(): TeamRoomEntity = TeamRoomEntity(
    id = id,
    longName = longName,
    population = population,
    worldCupTitles = worldCupTitles,
    registeredPlayers = registeredPlayers,
    wcParticipations = wcParticipations,
    worldRank = worldRank,
)
