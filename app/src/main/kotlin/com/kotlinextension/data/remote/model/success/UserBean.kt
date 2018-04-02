package com.kotlinextension.data.remote.model.success

import com.google.gson.annotations.SerializedName

data class UserBean(
    @SerializedName("results") val results: MutableList<User>,
    @SerializedName("info") val info: Info
)
