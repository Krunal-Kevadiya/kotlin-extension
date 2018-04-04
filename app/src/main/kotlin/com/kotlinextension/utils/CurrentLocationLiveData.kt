package com.kotlinextension.utils

import android.arch.lifecycle.LiveData
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.extensions.general.Locations

class CurrentLocationLiveData(var context: Context, var isFusedLocationApi: Boolean) :LiveData<Location>() {
	lateinit var locationService:Locations
	var mBound = false

	override fun onActive() {
		val serviceIntent = Intent(context, Locations::class.java)
		serviceIntent.putExtra(Locations.ARG_FUSED_LOCATION, isFusedLocationApi)
		context.startService(serviceIntent)
		context.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE)
	}

	override fun onInactive() {
		if (mBound) {
			context.unbindService(mConnection)
			mBound = false
		}
	}

	private val mConnection = object :ServiceConnection {
		override fun onServiceConnected(className:ComponentName, service:IBinder) {
			val binder = service as Locations.LocalBinder
			locationService = binder.service
			mBound = true

			if (mBound)
				locationService.setLocationCallback(object: Locations.LocationCallback{
					override fun locationDisabled() {
                        Log.e(CurrentLocationLiveData::class.java.simpleName, "Locations Disable")
                    }
					override fun onSuccess(location :Location) {
						value = location
					}
					override fun onFailure(e :Exception) {
                        Log.e(CurrentLocationLiveData::class.java.simpleName, "Locations Failure $e")
                    }
				})
		}

		override fun onServiceDisconnected(arg0:ComponentName) {
			mBound = false
		}
	}
}
