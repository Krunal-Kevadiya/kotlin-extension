package com.kotlinextension.ui.map

import com.kotlinextension.networking.NetworkService
import dagger.Module
import dagger.Provides

@Module
class MapsActivityModule {
	@Provides
	internal fun provideMainViewModel(networkService: NetworkService): MapsViewModel {
		return MapsViewModel(networkService)
	}
}
