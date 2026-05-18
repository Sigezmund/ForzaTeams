package com.forza.teams.data.network

import retrofit2.http.GET

interface TeamsApi {
    @GET("world-cup-teams.json")
    suspend fun getTeams(): List<TeamNetworkDto>
}
