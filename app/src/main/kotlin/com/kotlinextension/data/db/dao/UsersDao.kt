package com.kotlinextension.data.db.dao

import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Locations
import com.kotlinextension.data.db.entity.User
import io.reactivex.Flowable

@Dao
interface UsersDao {

    @Query("SELECT * from Users WHERE id = :id")
    fun getUserById(id: Long): Flowable<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()

    @Query("DELETE FROM Users WHERE id = :id")
    fun deleteUser(id: Long)

    @Delete
    fun deleteUser(user: User)

    @Delete
    fun deleteUsers(vararg user: User)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flowable<MutableList<User>>

    @Insert
    fun insertUserAndLocations(user: User, locations: MutableList<Locations>)
}
