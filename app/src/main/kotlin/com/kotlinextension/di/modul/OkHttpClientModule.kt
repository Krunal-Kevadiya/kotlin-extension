package com.kotlinextension.di.modul

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [AppModule::class, HostSelectionInterceptor::class])
class OkHttpClientModule {
    @Singleton
    @Provides
    fun cache(file :File) :Cache {
        return Cache(file, 10 * 1000 * 1000) // 10MB cache file
    }

    @Singleton
    @Provides
    fun file(context :Context) :File {
        val file = File(context.cacheDir, "restapi-cache")
        file.mkdirs()
        return file
    }

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
    internal fun providesOkHttpClientWithCache(loggingInterceptor: HttpLoggingInterceptor, hostSelectionInterceptor: HostSelectionInterceptor, cache :Cache): OkHttpClient {
		val timeout = 3

		return okhttp3.OkHttpClient.Builder()
            .cache(cache)
			.addInterceptor(loggingInterceptor)
			.addInterceptor(hostSelectionInterceptor)
			.readTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.writeTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.connectTimeout(timeout.toLong(), TimeUnit.MINUTES)
			.build()
	}
}
