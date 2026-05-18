package com.forza.teams.di

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.forza.teams.data.network.TeamFlagMapper
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val imageModule = module {
    factory {
        val context = androidContext()
        ImageLoader.Builder(context)
            .components { add(TeamFlagMapper("https://images.multiball.forzafootball.net/badges/team/thumbnail/")) }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("images_cache").toOkioPath())
                    .maxSizeBytes(512L * 1024 * 1024)
                    .build()
            }
            .build()
    }
}
