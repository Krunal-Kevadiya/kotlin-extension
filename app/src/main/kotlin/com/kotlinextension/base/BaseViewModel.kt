package com.kotlinextension.base

import android.arch.lifecycle.ViewModel
import com.kotlinextension.data.DataSource

abstract class BaseViewModel<N>(val dataSource: DataSource) : ViewModel() {
	var navigator: N? = null
}
