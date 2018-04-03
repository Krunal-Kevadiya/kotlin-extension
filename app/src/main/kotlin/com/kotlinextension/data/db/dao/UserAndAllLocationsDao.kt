package com.kotlinextension.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.kotlinextension.data.db.model.UserAndAllLocation
import io.reactivex.Flowable

@Dao
interface UserAndAllLocationsDao {
    @Transaction @Query("SELECT * FROM Users")
    fun loadUsersAndPets(): Flowable<List<UserAndAllLocation>>

    @Transaction @Query("SELECT * FROM Users WHERE id = :id")
    fun loadUserAndPets(id: String): Flowable<UserAndAllLocation>
}
