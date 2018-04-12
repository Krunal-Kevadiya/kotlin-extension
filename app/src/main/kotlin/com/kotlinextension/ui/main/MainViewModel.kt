package com.kotlinextension.ui.main

import android.util.Log
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.FailerApiResponse
import com.kotlinextension.data.db.datasource.LocalUserDataSource
import com.kotlinextension.data.db.entity.User
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(val dataSource :LocalUserDataSource):
    BaseViewModel<MainNavigator>() {

    fun getAllUsers() :Flowable<MutableList<User>> {
        return dataSource.getAll(object :FailerApiResponse {
            override fun onError(throwable :Throwable) {
                Log.e("MainViewModel", throwable.localizedMessage)

            }
        })
    }
}
