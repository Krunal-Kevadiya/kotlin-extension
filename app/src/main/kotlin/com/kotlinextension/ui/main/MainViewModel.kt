package com.kotlinextension.ui.main

import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.DatabaseSource
import com.kotlinextension.data.db.entity.User
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(val dataSource: DatabaseSource): BaseViewModel<MainNavigator>() {

    fun getAllUsers() :Flowable<MutableList<User>> {
        return dataSource.getAll()
    }
}
