package com.khaled.elmenus.di

import com.khaled.elmenus.common.IApplicationContext
import org.koin.java.KoinJavaComponent

object AppContext {
    val applicationContext by KoinJavaComponent.getKoin().inject<IApplicationContext>()
}