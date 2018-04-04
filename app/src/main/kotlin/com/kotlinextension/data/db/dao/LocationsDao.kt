package com.kotlinextension.data.db.dao

import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Locations
import io.reactivex.Flowable

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(vararg pets: Locations)

    @Delete
    fun deleteLocations(vararg pets: Locations)

    @Query("SELECT * FROM Locations")
    fun loadAllLocations(): Flowable<MutableList<Locations>>

    @Query("SELECT * FROM Locations WHERE id = :id")
    fun loadLocationById(id: Int): Flowable<Locations>
}
