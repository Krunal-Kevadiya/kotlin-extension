package com.kotlinextension.ui.main

import android.arch.lifecycle.LiveData
import android.content.Context
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.networking.NetworkService
import com.kotlinextension.networking.model.custom.ApiResponse
import com.kotlinextension.networking.model.success.ResSearchRepo
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainViewModel @Inject constructor(networkService: NetworkService) : BaseViewModel<MainNavigator>(networkService) {
	private lateinit var data: LiveData<ApiResponse<ResSearchRepo>>

	fun searchRepositories(): LiveData<ApiResponse<ResSearchRepo>> {
		if (!::data.isInitialized) {
			data = networkService.searchRepositories(
				hashMapOf(
					"q" to "kotlin:assembly",
					"sort" to "stars",
					"order" to "desc"))
			return data
		}
		return data
	}
}
