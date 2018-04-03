package com.kotlinextension.ui.main

import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.DatabaseSource
import com.kotlinextension.data.db.entity.User
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(dataSource: DatabaseSource<User>) : BaseViewModel<MainNavigator>(dataSource) {

    fun getAllUsers() :Flowable<MutableList<User>> {
        return dataSource.getAll()
    }
}
