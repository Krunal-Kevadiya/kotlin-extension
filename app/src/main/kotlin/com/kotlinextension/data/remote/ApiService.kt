package com.kotlinextension.data.remote

import com.kotlinextension.data.db.entity.User
import com.kotlinextension.data.remote.model.custom.ResultBean
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api")
    fun getUsers(@Query("results") size : Int):Flowable<ResultBean<Array<User>>>

}
