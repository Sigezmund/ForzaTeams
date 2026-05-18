package com.forza.teams.di

import com.forza.teams.presentation.teams.list.TeamsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { TeamsListViewModel(get()) }
}