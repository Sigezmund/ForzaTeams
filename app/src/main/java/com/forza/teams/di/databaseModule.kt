package com.forza.teams.di

import androidx.room.Room
import com.forza.teams.data.local.AppDatabase
import com.forza.teams.data.local.LocalTeamsDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "forza_teams.db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().teamsDao() }
    single { LocalTeamsDataSource(get()) }
}
