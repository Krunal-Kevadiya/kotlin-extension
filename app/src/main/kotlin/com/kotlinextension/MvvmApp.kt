package com.kotlinextension

import android.app.Activity
import android.app.Application
import com.kotlinextension.di.AppInjector
import com.kotlinextension.di.component.AppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MvvmApp : Application(), HasActivityInjector {
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
        @Inject set

    lateinit var appComponent: AppComponent

    override fun onCreate() {
		super.onCreate()
        AppInjector.initInjector(this)
	}

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityInjector
    }
}
