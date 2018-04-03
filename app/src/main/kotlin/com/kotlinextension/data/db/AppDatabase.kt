package com.kotlinextension.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.kotlinextension.data.db.dao.LocationsDao
import com.kotlinextension.data.db.dao.UserAndAllLocationsDao
import com.kotlinextension.data.db.dao.UsersDao
import com.kotlinextension.data.db.entity.Location
import com.kotlinextension.data.db.entity.User

@Database(entities = [(User::class), (Location::class)], version = DatabaseAnnotation.DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    abstract fun locationssDao(): LocationsDao

    abstract fun userAndLocationsDao(): UserAndAllLocationsDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this, {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                })


        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DatabaseAnnotation.DATABASE_NAME)
                        .build()
    }
}
