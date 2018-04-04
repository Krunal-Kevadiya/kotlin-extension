package com.kotlinextension.base

import android.arch.lifecycle.ViewModel
import com.kotlinextension.data.db.DatabaseSource
import com.kotlinextension.data.db.entity.User

abstract class BaseViewModel<N> : ViewModel() {
	var navigator: N? = null
}
