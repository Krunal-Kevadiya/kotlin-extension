package com.kotlinextension.base

import android.arch.lifecycle.ViewModel
import com.kotlinextension.networking.NetworkService
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<N>(val networkService: NetworkService) : ViewModel() {
	private val compositeDisposable: CompositeDisposable = CompositeDisposable()
	var navigator: N? = null

	override fun onCleared() {
		compositeDisposable.dispose()
		super.onCleared()
	}
}
