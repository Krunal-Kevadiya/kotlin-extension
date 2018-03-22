package com.kotlinextension.di.modul

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kotlinextension.BuildConfig
import com.kotlinextension.networking.NetworkService
import com.kotlinextension.networking.adapter.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
internal object NetworkModule {

	@Singleton
	@Provides
	@JvmStatic
	fun provideBaseUrlString(): String {
		return BuildConfig.BASEURL_API
	}

	@Singleton
	@Provides
	@JvmStatic
	fun providesGson(): Gson {
		return GsonBuilder().create()
	}

	@Singleton
	@Provides
	@JvmStatic
	fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
		return GsonConverterFactory.create(gson)
	}

	@Singleton
	@Provides
	@JvmStatic
	fun provideBaseRetrofitWithCache(baseUrl: String, okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
		return Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(okHttpClient)
				.addConverterFactory(gsonConverterFactory)
				.addCallAdapterFactory(LiveDataCallAdapterFactory()).build()
	}

	@Singleton
	@Provides
	@JvmStatic
	fun providesNetworkServiceWithCache(builder: Retrofit): NetworkService {
		return builder.create(NetworkService::class.java)
	}
}
