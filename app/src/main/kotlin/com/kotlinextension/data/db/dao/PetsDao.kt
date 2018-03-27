package com.kotlinextension.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Pet

@Dao
interface PetsDao {

    /**
     * Inserts a variable number of Pets
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPets(vararg pets: Pet)

    /**
     * Deletes a variable number of pets
     * */
    @Delete
    fun deletePets(vararg pets: Pet)

    /**
     * Loads all the pets in the database
     * */
    @Query("SELECT * FROM Pets")
    fun loadAllPets(): LiveData<List<Pet>>

    /**
     * Loads a single  matching a pet id
     * */
    @Query("SELECT * FROM Pets WHERE id = :id")
    fun loadPetById(id: Int): LiveData<Pet>
}
