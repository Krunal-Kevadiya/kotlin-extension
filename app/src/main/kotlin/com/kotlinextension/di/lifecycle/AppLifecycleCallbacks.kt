package com.kotlinextension.di.lifecycle

import android.app.Application

interface AppLifecycleCallbacks {
    fun onCreate(application :Application)
    fun onTerminate(application :Application)
}
