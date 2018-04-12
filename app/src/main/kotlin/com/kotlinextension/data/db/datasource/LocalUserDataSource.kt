package com.kotlinextension.data.db.datasource

import com.kotlinextension.data.db.FailerApiResponse
import com.kotlinextension.data.db.dao.UsersDao
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class LocalUserDataSource(private val userDao :UsersDao, private val apiService :ApiService) {
    fun get(bean :User) :Flowable<User> {
        return userDao.getUserById(bean.id)
    }

    fun getAll(error: FailerApiResponse) :Flowable<MutableList<User>> {
        val apiResult = apiService.getUsers(50)
        val dbResult = userDao.getAllUsers()

        return Flowable.combineLatest(apiResult, dbResult, )
        val stream = Flowable.create<Int>({subscriber ->
            apiService.getUsers(50)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    it?.let {
                        userDao.insertUser(*it)
                    }
                }, {
                    error.onError(it)
                })
        }, BackpressureStrategy.MISSING)

        stream.subscribe(
            {`val` -> log.info("Subscriber received: {}", `val`)},
            {err -> log.error("Subscriber received error", err)}
        ) {log.info("Subscriber got Completed event")}

        apiService.getUsers(50)
            .subscribeOn(Schedulers.io())
            .subscribe({
                it?.let {
                   userDao.insertUser(*it)
                }
            }, {
                error.onError(it)
            })

        return userDao.getAllUsers()
    }

    fun insert(bean :User) {
        userDao.insertUser(bean)
    }

    fun delete(bean :User) {
        userDao.deleteUser(bean.id)
    }

    fun update(bean :User) {
        userDao.updateUser(bean)
    }
}
