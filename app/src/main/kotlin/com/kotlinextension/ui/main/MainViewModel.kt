package com.kotlinextension.ui.main

import android.arch.lifecycle.LiveData
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.DataSource
import javax.inject.Inject

class MainViewModel @Inject constructor(dataSource: DataSource) : BaseViewModel<MainNavigator>(dataSource) {
    fun loadUsers(): LiveData<List<User>> {
        return dataSource.loadUsers()
    }
}
