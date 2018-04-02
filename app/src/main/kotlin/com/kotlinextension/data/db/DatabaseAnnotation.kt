package com.kotlinextension.data.db

import android.support.annotation.StringDef

class DatabaseAnnotation {

    companion object {
        @StringDef(ID, USER_NAME, GENDER, EMAIL, PASSWORD, DOB, REGISTERED, PHONE, CELL, PICTURE, NAT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class User

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
    }
}
