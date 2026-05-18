package com.forza.teams.data

import com.forza.teams.data.local.LocalTeamsDataSource
import com.forza.teams.data.network.NetworkTeamsDataSource
import com.forza.teams.domain.model.TeamEntity
import com.forza.teams.domain.model.TeamsError
import com.forza.teams.domain.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TeamsRepositoryImpl(
    private val networkDataSource: NetworkTeamsDataSource,
    private val localDataSource: LocalTeamsDataSource,
) : TeamsRepository {

    override fun observeTeams(): Flow<List<TeamEntity>> =
        localDataSource.observeTeams()

    override suspend fun refreshTeams(): Result<Unit> {
        val result = runCatching { localDataSource.saveTeams(networkDataSource.getTeams()) }
        return result.exceptionOrNull()?.let { Result.failure(it.toTeamsError()) } ?: result
    }

    private fun Throwable.toTeamsError(): TeamsError = when (this) {
        is UnknownHostException, is ConnectException -> TeamsError.NoConnection
        is SocketTimeoutException -> TeamsError.Timeout
        is HttpException -> TeamsError.ServerError(code())
        is SerializationException -> TeamsError.ParseError
        else -> TeamsError.Unknown
    }
}
