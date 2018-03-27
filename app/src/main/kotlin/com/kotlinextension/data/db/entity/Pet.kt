package com.kotlinextension.data.db.entity

import android.arch.persistence.room.*

@Entity(
    tableName = "pets",
    foreignKeys = [(ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE))],
    indices = [(Index("user_id"))])
data class Pet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "user_id")
    val userId: String)
