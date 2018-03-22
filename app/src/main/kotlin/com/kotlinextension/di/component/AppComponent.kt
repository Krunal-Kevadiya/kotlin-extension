package com.kotlinextension.di.component

import com.kotlinextension.MvvmApp
import com.kotlinextension.di.UiModule
import com.kotlinextension.di.modul.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
	AndroidSupportInjectionModule::class,
	AppModule::class,
	UiModule::class
])
interface AppComponent : AndroidInjector<MvvmApp> {

	@Component.Builder
	interface Builder {
		@BindsInstance
		fun application(application: MvvmApp): Builder

		fun build(): AppComponent
	}

	override fun inject(app: MvvmApp)
}
