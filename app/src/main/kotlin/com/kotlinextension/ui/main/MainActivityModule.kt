package com.kotlinextension.ui.main

import com.kotlinextension.networking.NetworkService
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {
	@Provides
	internal fun provideMainViewModel(networkService: NetworkService): MainViewModel {
		return MainViewModel(networkService)
	}
}
