package com.khaled.elmenus.di

import com.khaled.elmenus.MainViewModel
import com.khaled.elmenus.common.ApplicationContext
import com.khaled.elmenus.common.IApplicationContext
import com.khaled.elmenus.feature.home.module.data.HomeRepository
import com.khaled.elmenus.feature.home.module.data.IHomeRepository
import com.khaled.elmenus.feature.home.screen.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object FeaturesKoinModules {

    val allModules = ArrayList<Module>().apply {
        // application helpers module
        add(getAppHelperModule())
        // main screen
        add(getMainModule())
        // Home screen
        add(getHomeModule())
    }

    private fun getHomeModule() = module {
        factory<IHomeRepository> { HomeRepository() }
        viewModel { HomeViewModel() }
    }

    private fun getMainModule() = module { viewModel { MainViewModel() } }

    private fun getAppHelperModule() = module {
        single<IApplicationContext> { ApplicationContext(androidContext()) }
    }
}