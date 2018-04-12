package com.kotlinextension.data.datasource

import android.util.Log
import com.kotlinextension.data.db.dao.UsersDao
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.Flowable

class LocalUserDataSource(private val userDao :UsersDao, private val apiService :ApiService) {
    fun get(bean :User) :Flowable<User> {
        return userDao.getUserById(bean.id)
    }

    fun getAll() :Flowable<MutableList<User>> {
        return Flowable.generate<MutableList<User>> { emitter ->
            userDao.getAllUsers().subscribe {
                emitter.onNext(it)
            }
            apiService.getUsers(50)
                .subscribe({
                    it?.let {
                        userDao.insertUser(*it.result)
                        emitter.onComplete()
                    }
                }, {
                    emitter.onError(it)
                    emitter.onComplete()
                })
        }
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
