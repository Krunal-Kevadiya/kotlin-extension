package com.kotlinextension.data.db.dao

import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Locations
import io.reactivex.Flowable

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(pets: Locations) :Long

    @Delete
    fun deleteLocations(vararg pets: Locations) :Int

    @Query("SELECT * FROM Locations")
    fun loadAllLocations(): Flowable<MutableList<Locations>>

    @Query("SELECT * FROM Locations WHERE id = :id")
    fun loadLocationById(id: Int): Flowable<Locations>
}
