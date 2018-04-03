package com.kotlinextension.base

import android.arch.lifecycle.ViewModel

abstract class BaseViewModel<N>(val dataSource: DataSource) : ViewModel() {
	var navigator: N? = null
}
