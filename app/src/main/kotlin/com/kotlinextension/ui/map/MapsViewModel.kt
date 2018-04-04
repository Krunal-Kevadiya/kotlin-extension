package com.kotlinextension.ui.map

import android.content.Context
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.utils.CurrentLocationLiveData
import javax.inject.Inject

class MapsViewModel @Inject constructor(): BaseViewModel<MapsNavigator>() {
	lateinit var location:CurrentLocationLiveData

	fun initLocation(context :Context) {
		location = CurrentLocationLiveData(context, true)
	}
}
