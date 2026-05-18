package com.forza.teams.di

import com.forza.teams.data.TeamsRepositoryImpl
import com.forza.teams.domain.repository.TeamsRepository
import com.forza.teams.data.network.NetworkTeamsDataSource
import com.forza.teams.data.network.TeamsApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://frz-campaign-forzafootball.s3.eu-west-1.amazonaws.com/"

private val json = Json { ignoreUnknownKeys = true }

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single { get<Retrofit>().create(TeamsApi::class.java) }
    single { NetworkTeamsDataSource(get()) }
    single<TeamsRepository> { TeamsRepositoryImpl(get(), get()) }
}
