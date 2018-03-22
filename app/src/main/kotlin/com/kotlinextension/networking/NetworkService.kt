package com.kotlinextension.networking

import android.arch.lifecycle.LiveData
import com.kotlinextension.networking.model.custom.ApiResponse
import com.kotlinextension.networking.model.success.ResSearchRepo
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NetworkService {
	@GET("search/repositories")
	fun searchRepositories(@QueryMap query: Map<String, String>): LiveData<ApiResponse<ResSearchRepo>>
}
