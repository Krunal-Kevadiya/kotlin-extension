package com.kotlinextension.data.db.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.kotlinextension.data.db.entity.Location
import com.kotlinextension.data.db.entity.User

class UserAndAllLocation {
    @Embedded
    var user: User? = null

    @Relation(parentColumn = "id", entityColumn = "user_id")
    var locations: List<Location>? = null
}
