package com.kotlinextension.data.remote.model.custom

import com.google.gson.annotations.SerializedName

data class ResultBean<out T>(
    @SerializedName("status") val status :Int,
    @SerializedName("message") val message :String,
    @SerializedName("results") val result :T
)
