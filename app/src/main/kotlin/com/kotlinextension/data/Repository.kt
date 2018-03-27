package com.kotlinextension.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.kotlinextension.data.DataSource
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.ApiService

open class Repository(val apiService: ApiService) : DataSource {

    /**
     * Loads s list fo users
     * */
    override fun loadUsers(): LiveData<List<User>> {
        return Transformations.map(apiService.getUsers(), { response -> response.body })

    }
}
