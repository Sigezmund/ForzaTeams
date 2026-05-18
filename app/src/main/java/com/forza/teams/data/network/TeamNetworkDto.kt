package com.forza.teams.data.network

import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamFlagEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamNetworkDto(
    val name: String,
    @SerialName("team_id")
    val teamId: Int,
    @SerialName("population")
    val population: Int,
    @SerialName("world_cup_titles")
    val worldCupTitles: Int,
    @SerialName("registered_players")
    val registeredPlayers: Int,
    @SerialName("wc_participations")
    val wcParticipations: Int,
    @SerialName("world_rank")
    val worldRank: Int,
)

fun TeamNetworkDto.toEntity() = TeamEntity(
    id = teamId.toString(),
    flag = TeamFlagEntity(teamId.toString()),
    longName = name,
    population = population,
    worldCupTitles = worldCupTitles,
    registeredPlayers = registeredPlayers,
    wcParticipations = wcParticipations,
    worldRank = worldRank,
)
