package com.kotlinextension

import com.kotlinextension.di.applyAutoInjector
import com.kotlinextension.di.component.DaggerAppComponent
import com.kotlinextension.di.lifecycle.AppLifecycleCallbacks
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MvvmApp : DaggerApplication() {
	@Inject
	lateinit var appLifecycleCallbacks: AppLifecycleCallbacks

	override fun applicationInjector() = DaggerAppComponent.builder()
		.application(this)
		.build()

	override fun onCreate() {
		super.onCreate()
		applyAutoInjector()
		appLifecycleCallbacks.onCreate(this)
	}

	override fun onTerminate() {
		appLifecycleCallbacks.onTerminate(this)
		super.onTerminate()
	}
}
