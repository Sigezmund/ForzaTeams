package com.forza.teams.domain.model

data class TeamEntity(
    val id: String,
    val flag: TeamFlagEntity,
    val longName: String,
    val population: Int,
    val worldCupTitles: Int,
    val registeredPlayers: Int,
    val wcParticipations: Int,
    val worldRank: Int,
)

data class TeamFlagEntity(val teamId: String)
