package com.kotlinextension.di.builder

import com.kotlinextension.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.kotlinextension.di.scope.PerActivity

@Module
abstract class BuildersModule {
    @PerActivity
	@ContributesAndroidInjector
	abstract fun contributeMainActivity(): MainActivity
}
