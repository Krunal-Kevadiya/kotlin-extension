package com.kotlinextension.di.builder

import android.arch.lifecycle.ViewModel
import com.kotlinextension.di.viewmodel.ViewModelKey
import com.kotlinextension.ui.main.MainViewModel
import com.kotlinextension.ui.map.MapsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ActivityBuilder {
	@Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(MapsViewModel::class)
	abstract fun bindMapsViewModel(viewModel: MapsViewModel): ViewModel

	//@ContributesAndroidInjector
	//abstract fun contributeMainFragment(): MainFragment
}
