package com.kotlinextension.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kotlinextension.di.scope.ViewModelKey
import com.kotlinextension.ui.main.MainViewModel
import com.kotlinextension.ui.map.MapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    /**
     * Provides the MyViewModelFactory
     * */
    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(MapsViewModel::class)
	abstract fun bindMapsViewModel(viewModel: MapsViewModel): ViewModel
}
