package com.kotlinextension.di.modul

import android.content.Context
import com.kotlinextension.MvvmApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DataSourceModule::class])
class AppModule(val app: MvvmApp) {
    @Singleton
    @Provides
    internal fun provideContext(): Context {
        return app.applicationContext
    }
}
