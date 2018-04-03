package com.kotlinextension.di.component

import com.kotlinextension.MvvmApp
import com.kotlinextension.di.builder.BuildersModule
import com.kotlinextension.di.modul.AppModule
import com.kotlinextension.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
	AndroidSupportInjectionModule::class,
	AppModule::class,
    BuildersModule::class,
    ViewModelModule::class
])
interface AppComponent {

	@Component.Builder
	interface Builder {
		@BindsInstance
		fun application(application: MvvmApp): Builder

        fun appModule(appModule: AppModule): Builder

		fun build(): AppComponent
	}

	fun inject(app: MvvmApp)
}
