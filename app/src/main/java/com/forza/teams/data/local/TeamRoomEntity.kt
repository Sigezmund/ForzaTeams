package com.forza.teams.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamRoomEntity(
    @PrimaryKey val id: String,
    val longName: String,
    val population: Int,
    val worldCupTitles: Int,
    val registeredPlayers: Int,
    val wcParticipations: Int,
    val worldRank: Int,
)
