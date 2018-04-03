package com.kotlinextension.data.db

import android.support.annotation.StringDef

class DatabaseAnnotation {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "AppDatabase.db"

        @StringDef(ID, USER_NAME, GENDER, EMAIL, PASSWORD, DOB, REGISTERED, PHONE, CELL, PICTURE, NAT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class UserTable

        const val TABLE_USER = "users"
        const val ID = "id"
        const val USER_NAME = "username"
        const val GENDER = "gender"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val DOB = "dob"
        const val REGISTERED = "registered"
        const val PHONE = "phone"
        const val CELL = "cell"
        const val PICTURE = "picture"
        const val NAT = "nat"

        const val TABLE_LOCATION = "locations"
        const val USER_ID = "user_id"
        const val STREET = "street"
        const val CITY = "city"
        const val STATE = "state"
        const val POSTCODE = "postcode"
    }
}
