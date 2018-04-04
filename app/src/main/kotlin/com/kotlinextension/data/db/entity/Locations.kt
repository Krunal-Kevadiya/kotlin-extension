package com.kotlinextension.data.db.entity

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import com.kotlinextension.data.db.DatabaseAnnotation

@Entity(
    tableName = DatabaseAnnotation.TABLE_LOCATION,
    foreignKeys = [(ForeignKey(entity = User::class,
        parentColumns = arrayOf(DatabaseAnnotation.ID),
        childColumns = arrayOf(DatabaseAnnotation.USER_ID),
        onDelete = ForeignKey.CASCADE))],
    indices = [(Index(DatabaseAnnotation.USER_ID))])
data class Locations constructor(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = DatabaseAnnotation.ID) val id: Long,
                                 @ColumnInfo(name = DatabaseAnnotation.USER_ID) val userId: Long,
                                 @SerializedName("street") @ColumnInfo(name = DatabaseAnnotation.STREET) val street: String,
                                 @SerializedName("city") @ColumnInfo(name = DatabaseAnnotation.CITY) val city: String,
                                 @SerializedName("state") @ColumnInfo(name = DatabaseAnnotation.STATE) val state: String,
                                 @SerializedName("postcode") @ColumnInfo(name = DatabaseAnnotation.POSTCODE) val postcode: String)
