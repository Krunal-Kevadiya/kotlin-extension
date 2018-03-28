package com.kotlinextension.di.builder

import com.kotlinextension.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.kotlinextension.di.scope.PerActivity
import com.kotlinextension.ui.map.MapsActivity

@Module
abstract class BuildersModule {
    @PerActivity
	@ContributesAndroidInjector
	abstract fun contributeMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector
    abstract fun contributeMapsActivity(): MapsActivity
}
