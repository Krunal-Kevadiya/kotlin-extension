package com.kotlinextension.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.kotlinextension.data.db.model.UserAndAllPets

@Dao
interface UserAndAllPetsDao {
    /**
     * Loads all the users and their pets
     *
     * @return List of [UserAndAllPets]
     * */
    @Transaction @Query("SELECT * FROM Users")
    fun loadUsersAndPets(): LiveData<List<UserAndAllPets>>


    /**
     * Loads a single user with list of hu=is pets as [UserAndAllPets]
     * @return [UserAndAllPets]
     * */
    @Transaction @Query("SELECT * FROM Users WHERE id = :id")
    fun loadUserAndPets(id: String): LiveData<UserAndAllPets>
}
