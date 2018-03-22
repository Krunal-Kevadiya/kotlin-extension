package com.kotlinextension.di

import android.arch.lifecycle.ViewModelProvider
import com.kotlinextension.di.builder.ActivityBuilder
import com.kotlinextension.di.viewmodel.ViewModelFactory
import com.kotlinextension.ui.main.MainActivity
import com.kotlinextension.ui.map.MapsActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class UiModule {

@Binds
abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

@ContributesAndroidInjector(modules = [ActivityBuilder::class])
internal abstract fun contributeMainActivity(): MainActivity

@ContributesAndroidInjector(modules = [ActivityBuilder::class])
internal abstract fun contributeMapsActivity(): MapsActivity
}
