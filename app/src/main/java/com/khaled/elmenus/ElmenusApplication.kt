package com.khaled.elmenus

import android.app.Application
import com.khaled.elmenus.di.FeaturesKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ElmenusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)
            androidContext(this@ElmenusApplication)
            modules(FeaturesKoinModules.allModules)
        }
    }
}