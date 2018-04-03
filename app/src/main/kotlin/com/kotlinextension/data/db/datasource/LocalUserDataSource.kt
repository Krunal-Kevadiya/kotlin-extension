package com.kotlinextension.data.db.datasource

import com.kotlinextension.data.db.DatabaseSource
import com.kotlinextension.data.db.dao.UsersDao
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class LocalUserDataSource(private val userDao :UsersDao, private val apiService :ApiService) :DatabaseSource<User> {
    override fun get(bean :User) :Flowable<User> {
        return userDao.getUserById(bean.id)
    }

    override fun getAll() :Flowable<MutableList<User>> {
        apiService.getUsers(50)
            .subscribeOn(Schedulers.io())
            .subscribe({
                it?.let {
                   userDao.insertUser(*it)
                }
            }, {

            })
        return userDao.getAllUsers()
    }

    override fun insert(bean :User) {
        userDao.insertUser(bean)
    }

    override fun delete(bean :User) {
        userDao.deleteUser(bean.id)
    }

    override fun update(bean :User) {
        userDao.updateUser(bean)
    }


}
