package com.kotlinextension.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @Flatten("login::username")
    @ColumnInfo(name = "name")
    val userName: String,

    @SerializedName("gender")
    @ColumnInfo(name = "gender")
    val gender: String,

    @SerializedName("location")
    @ColumnInfo(name = "name")
    val location: Location,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,

    @Flatten("login::password")
    @ColumnInfo(name = "password")
    val password: String,

    @SerializedName("dob")
    @ColumnInfo(name = "dob")
    val dob: String,

    @SerializedName("registered")
    @ColumnInfo(name = "registered")
    val registered: String,

    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    val phone: String,

    @SerializedName("cell")
    @ColumnInfo(name = "cell")
    val cell: String,

    @Flatten("picture::medium")
    @ColumnInfo(name = "picture")
    val picture: String,

    @SerializedName("nat")
    @ColumnInfo(name = "nat")
    val nat: String
)
