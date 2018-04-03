package com.kotlinextension.data.db.dao

import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Location
import io.reactivex.Flowable

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(vararg pets: Location)

    @Delete
    fun deleteLocations(vararg pets: Location)

    @Query("SELECT * FROM Locations")
    fun loadAllLocations(): Flowable<MutableList<Location>>

    @Query("SELECT * FROM Locations WHERE id = :id")
    fun loadLocationById(id: Int): Flowable<Location>
}
