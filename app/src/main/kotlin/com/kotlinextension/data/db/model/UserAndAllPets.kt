package com.kotlinextension.data.db.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.kotlinextension.data.db.entity.Pet
import com.kotlinextension.data.db.entity.User

class UserAndAllPets {
    @Embedded
    var user: User? = null

    @Relation(parentColumn = "id", entityColumn = "user_id")
    var pets: List<Pet>? = null
}
