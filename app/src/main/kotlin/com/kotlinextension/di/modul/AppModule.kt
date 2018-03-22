package com.kotlinextension.di.modul

import com.kotlinextension.di.lifecycle.AppLifecycleCallbacks
import com.kotlinextension.di.lifecycle.ReleaseAppLifecycleCallbacks
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
internal object AppModule {
	@Singleton
	@Provides
	@JvmStatic
	fun provideAppLifecycleCallbacks(): AppLifecycleCallbacks = ReleaseAppLifecycleCallbacks()
}
