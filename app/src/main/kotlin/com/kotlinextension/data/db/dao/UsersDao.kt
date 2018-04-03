package com.kotlinextension.data.db.dao

import android.arch.persistence.room.*
import com.kotlinextension.data.db.entity.Location
import com.kotlinextension.data.db.entity.User
import io.reactivex.Flowable

@Dao
interface UsersDao {

    @Query("SELECT * from Users WHERE id = :id")
    fun getUserById(id: String): Flowable<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()

    @Delete
    fun deleteUser(user: User)

    @Delete
    fun deleteUsers(vararg user: User)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flowable<List<User>>

    @Insert
    fun insertUserAndPets(user: User, pets: List<Location>)
}
