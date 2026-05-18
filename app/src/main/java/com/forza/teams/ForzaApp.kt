package com.forza.teams

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.forza.teams.di.databaseModule
import com.forza.teams.di.imageModule
import com.forza.teams.di.networkModule
import com.forza.teams.di.viewModelsModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ForzaApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ForzaApp)
            modules(viewModelsModule, networkModule, databaseModule, imageModule)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader = get()
}
