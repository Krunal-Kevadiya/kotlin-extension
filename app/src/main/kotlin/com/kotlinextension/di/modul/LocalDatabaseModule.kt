package com.kotlinextension.di.modul

import android.content.Context
import com.kotlinextension.data.db.AppDatabase
import com.kotlinextension.data.datasource.LocalUserDataSource
import com.kotlinextension.data.remote.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class, NetworkModule::class])
class LocalDatabaseModule {
    @Singleton
    @Provides
    internal fun providesLocalDbService(context :Context):AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    internal fun provideLocalUserDataSource(apiService :ApiService, localDbService :AppDatabase) :LocalUserDataSource {
        return LocalUserDataSource(localDbService.usersDao(), apiService)
    }
}
