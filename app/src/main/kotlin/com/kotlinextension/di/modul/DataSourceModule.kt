package com.kotlinextension.di.modul

import com.kotlinextension.data.DataSource
import com.kotlinextension.data.Repository
import com.kotlinextension.data.remote.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class DataSourceModule {
    @Singleton
    @Provides
    internal fun provideDataSource(apiService: ApiService): DataSource {
        return Repository(apiService)
    }
}
