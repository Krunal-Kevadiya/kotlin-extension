package com.kotlinextension.di.modul

import android.content.Context
import com.kotlinextension.MvvmApp
import com.kotlinextension.data.db.AppDatabase
import com.kotlinextension.data.db.DatabaseSource
import com.kotlinextension.data.db.datasource.LocalUserDataSource
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class/*, LocalDatabaseModule::class*/])
class AppModule(val app: MvvmApp) {
    @Singleton
    @Provides
    internal fun provideContext(): Context {
        return app.applicationContext
    }

    /*@Singleton
    @Provides
    internal fun provideLocalUserDataSource(apiService:ApiService, localDbService:AppDatabase):DatabaseSource<User> {
        return LocalUserDataSource(localDbService.usersDao(), apiService)
    }*/
}
