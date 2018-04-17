package com.kotlinextension.ui.main

import android.util.Log
import com.extensions.recyclerAdapter.RecyclerAdapter
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.AppDatabase
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val appDatabase :AppDatabase,
    private val apiService :ApiService) :BaseViewModel<MainNavigator>() {

    private val disposable :CompositeDisposable = CompositeDisposable()

    fun getAllDbUsers(adapter :RecyclerAdapter) {
        disposable.add(appDatabase.usersDao().getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                adapter.addAllWithClear(items)
            }, { throwable ->
                Log.e("Adapter", "error " + throwable.localizedMessage)
            }))
    }

    fun insertAllDbUsers(users :Array<User>)  {
        disposable.add(Completable.fromAction {
            users.forEach { user ->
                val userId = appDatabase.usersDao().insertUser(user)
                user.location!!.userId = userId
                appDatabase.locationssDao().insertLocations(user.location)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("Api", "success insert")
            }, { throwable ->
                Log.e("Api", "error " + throwable.localizedMessage)
            }))
    }

    fun deleteDbUser(position: Int, user :User, adapter :RecyclerAdapter) {
        disposable.add(Completable.fromAction {
            appDatabase.usersDao().deleteUser(user)
        }.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({
                 adapter.notifyItemRemoved(position)
             }, { throwable ->
                 Log.e("Api", "error " + throwable.localizedMessage)
             }))
    }

    fun getAllApiUsers() {
        disposable.add(apiService.getUsers(10)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                response?.let {
                    insertAllDbUsers(it.result)
                }
            }, { throwable ->
                Log.e("Api", "error " + throwable.localizedMessage)
            }))
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
