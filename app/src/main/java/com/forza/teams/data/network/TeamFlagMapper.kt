package com.forza.teams.data.network

import coil3.map.Mapper
import coil3.request.Options
import com.forza.teams.domain.model.TeamFlagEntity

class TeamFlagMapper(baseUrl: String) : Mapper<TeamFlagEntity, String> {
    private val base = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

    override fun map(data: TeamFlagEntity, options: Options): String =
        "$base${data.teamId}.png"
}
