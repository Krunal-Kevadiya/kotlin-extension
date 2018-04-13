package com.kotlinextension.ui.main

import android.util.Log
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.AppDatabase
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val appDatabase :AppDatabase,
    private val apiService :ApiService) :BaseViewModel<MainNavigator>() {

    fun getAllDbUsers() :Flowable<MutableList<User>> {
        return appDatabase.usersDao().getAllUsers()
    }

    fun getAllUsers() {
        apiService.getUsers(10)
            .subscribeOn(Schedulers.io())
            .subscribe({response ->
                response?.let {
                    appDatabase.usersDao().insertUser(*it.result)
                }
            }, {throwable ->
                Log.e("Api", "error " + throwable.localizedMessage)
            })
    }
}
