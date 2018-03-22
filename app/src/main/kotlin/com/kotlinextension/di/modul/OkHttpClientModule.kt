package com.kotlinextension.di.modul

import com.kotlinextension.networking.HostSelectionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [HostSelectionInterceptor::class])
internal object OkHttpClientModule {
	@Singleton
	@Provides
	@JvmStatic
	fun providesLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor()
			.apply { level = HttpLoggingInterceptor.Level.BODY }
	}

	@Singleton
	@Provides
	@JvmStatic
	fun provideHostSelectionInterceptor(): HostSelectionInterceptor {
		return HostSelectionInterceptor.get()
	}

	@Singleton
	@Provides
	@JvmStatic
	fun providesOkHttpClientWithCache(loggingInterceptor: HttpLoggingInterceptor, hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient {
		val timeout = 3

		return okhttp3.OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.addInterceptor(hostSelectionInterceptor)
			.readTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.writeTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.connectTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.build()
	}
}
