package com.kotlinextension.data.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kotlinextension.data.db.DatabaseAnnotation
import com.kotlinextension.data.remote.adapter.Flatten

@Entity(tableName = DatabaseAnnotation.TABLE_USER)
data class User constructor(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = DatabaseAnnotation.ID) val id: Long,
    @Flatten("login::username") @ColumnInfo(name = DatabaseAnnotation.USER_NAME) val userName: String,
    @SerializedName("gender") @ColumnInfo(name = DatabaseAnnotation.GENDER) val gender: String,
    @SerializedName("email") @ColumnInfo(name = DatabaseAnnotation.EMAIL) val email: String,
    @Flatten("login::password") @ColumnInfo(name = DatabaseAnnotation.PASSWORD) val password: String,
    @SerializedName("dob") @ColumnInfo(name = DatabaseAnnotation.DOB) val dob: String,
    @SerializedName("registered") @ColumnInfo(name = DatabaseAnnotation.REGISTERED) val registered: String,
    @SerializedName("phone") @ColumnInfo(name = DatabaseAnnotation.PHONE) val phone: String,
    @SerializedName("cell") @ColumnInfo(name = DatabaseAnnotation.CELL) val cell: String,
    @Flatten("picture::medium") @ColumnInfo(name = DatabaseAnnotation.PICTURE) val picture: String,
    @SerializedName("nat") @ColumnInfo(name = DatabaseAnnotation.NAT) val nat: String)
/*
    @SerializedName("location")
    @Ignore
    val location: Location,
*/


