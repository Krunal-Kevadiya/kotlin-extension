package com.kotlinextension.data

import android.arch.lifecycle.LiveData
import com.kotlinextension.data.db.entity.User

interface DataSource {
    /**
     * Loads a list of users
     * */
    fun loadUsers(): LiveData<List<User>>
}
