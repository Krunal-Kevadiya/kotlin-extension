package com.kotlinextension.data.db

import io.reactivex.Flowable

interface DatabaseSource<T> {
    fun getAll() :Flowable<MutableList<T>>
    fun get(bean :T) :Flowable<T>
    fun insert(bean :T)
    fun delete(bean :T)
    fun update(bean :T)
}
