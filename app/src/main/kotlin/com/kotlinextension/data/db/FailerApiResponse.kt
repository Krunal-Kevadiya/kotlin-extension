package com.kotlinextension.data.db

/**
 * Created by Krunal on 07-04-2018.
 */
interface FailerApiResponse {
    fun onError(throwable: Throwable)
}
