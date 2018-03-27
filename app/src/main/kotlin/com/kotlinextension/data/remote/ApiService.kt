package com.kotlinextension.data.remote

import android.arch.lifecycle.LiveData
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.model.custom.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    /**
     * Returns a list of users from the server
     * */
    @GET("users")
    fun getUsers(): LiveData<ApiResponse<List<User>>>


    /**
     * Returns a user given the uui
     * */
    @GET("users/{uuid}")
    fun getUser(@Path("uuid") userId: String): LiveData<ApiResponse<User>>

}
