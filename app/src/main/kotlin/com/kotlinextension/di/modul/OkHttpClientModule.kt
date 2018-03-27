package com.kotlinextension.di.modul

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [HostSelectionInterceptor::class])
class OkHttpClientModule {
	@Singleton
	@Provides
    internal fun providesLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor()
			.apply { level = HttpLoggingInterceptor.Level.BODY }
	}

	@Singleton
	@Provides
    internal fun provideHostSelectionInterceptor(): HostSelectionInterceptor {
		return HostSelectionInterceptor.get()
	}

	@Singleton
	@Provides
    internal fun providesOkHttpClientWithCache(loggingInterceptor: HttpLoggingInterceptor, hostSelectionInterceptor: HostSelectionInterceptor): OkHttpClient {
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
