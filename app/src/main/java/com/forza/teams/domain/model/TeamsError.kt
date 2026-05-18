package com.forza.teams.domain.model

sealed class TeamsError : Exception() {
    object NoConnection : TeamsError()
    object Timeout : TeamsError()
    data class ServerError(val code: Int) : TeamsError()
    object ParseError : TeamsError()
    object Unknown : TeamsError()
}
