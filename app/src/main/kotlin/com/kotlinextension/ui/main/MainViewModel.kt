package com.kotlinextension.ui.main

import android.util.Log
import com.extensions.reactive.runSafeOnIO
import com.extensions.reactive.runSafeOnMain
import com.extensions.reactive.smartSubscribe
import com.extensions.recyclerAdapter.RecyclerAdapter
import com.kotlinextension.base.BaseViewModel
import com.kotlinextension.data.db.AppDatabase
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val appDatabase :AppDatabase,
    private val apiService :ApiService) :BaseViewModel<MainNavigator>() {

    private val disposable :CompositeDisposable = CompositeDisposable()

    fun getAllDbUsers(adapter :RecyclerAdapter) {
        disposable.add(appDatabase.usersDao().getAllUsers()
            .runSafeOnMain()
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
        }.runSafeOnMain()
            .subscribe({
                Log.e("Api", "success insert")
            }, { throwable ->
                Log.e("Api", "error " + throwable.localizedMessage)
            }))
    }

    fun deleteDbUser(position: Int, user :User, adapter :RecyclerAdapter) {
        disposable.add(Completable.fromAction {
            appDatabase.usersDao().deleteUser(user)
        }.runSafeOnMain()
             .subscribe({
                 //adapter.notifyItemRemoved(position)
             }, { throwable ->
                 Log.e("Api", "error " + throwable.localizedMessage)
             }))
    }

    fun getAllApiUsers() {
        disposable.add(apiService.getUsers(10)
            .runSafeOnIO()
            .smartSubscribe(
                onStart = { Log.e("Api", "OnStart") },
                onSuccess = {
                    Log.e("Api", "OnSuccess")
                    it?.let {
                        insertAllDbUsers(it.result)
                    }
                },
                onError = { Log.e("Api", "OnError " + it.localizedMessage) },
                onFinish = { Log.e("Api", "OnFinish") }
            ))
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
