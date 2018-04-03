package com.kotlinextension.di.modul

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kotlinextension.BuildConfig
import com.kotlinextension.data.remote.ApiService
import com.kotlinextension.data.remote.adapter.FlattenTypeAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
class NetworkModule {

	@Singleton
	@Provides
	internal fun provideBaseUrlString(): String {
		return BuildConfig.BASEURL_API
	}

	@Singleton
	@Provides
	internal fun providesGson(): Gson {
		return GsonBuilder()
            .registerTypeAdapterFactory(FlattenTypeAdapterFactory()).create()
	}

	@Singleton
	@Provides
    internal fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
		return GsonConverterFactory.create(gson)
	}

	@Singleton
	@Provides
    internal fun provideBaseRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
		return Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(okHttpClient)
				.addConverterFactory(gsonConverterFactory)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
	}

	@Singleton
	@Provides
    internal fun providesApiService(builder: Retrofit): ApiService {
		return builder.create(ApiService::class.java)
	}
}
