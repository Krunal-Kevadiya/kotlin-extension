package com.kotlinextension.di.modul

import dagger.Module
import okhttp3.HttpUrl
import okhttp3.Interceptor
import java.io.IOException
import javax.inject.Inject

@Module
class HostSelectionInterceptor : Interceptor {

	companion object {
		private var httpUrl: HttpUrl? = null
		private var sInterceptor: HostSelectionInterceptor? = null

		fun get(): HostSelectionInterceptor {
			if (sInterceptor == null) {
				sInterceptor = HostSelectionInterceptor()
			}
			return sInterceptor as HostSelectionInterceptor
		}
	}

	@Inject
	constructor()

	fun setHost(argUrl: String) {
		httpUrl = HttpUrl.parse(argUrl)
	}

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
		var request = chain.request()
		if (httpUrl != null) {
			val newUrl = request.url().newBuilder()
					.scheme(httpUrl!!.scheme())
					.host(httpUrl!!.host())

			request = request.newBuilder()
					.url(newUrl.build())
					.build()
		}
		return chain.proceed(request)
	}
}
