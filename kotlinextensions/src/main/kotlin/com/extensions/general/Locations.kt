package com.extensions.general

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Surface
import com.extensions.content.locationManager
import com.extensions.content.sensorManager
import com.extensions.content.windowManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class Locations : Service() {
    private val localBinder = LocalBinder()
    private val sensorEventListener = SensorListener()
    private val fusedLocationListener = MyLocationListener()
    private val gpsLocationListener = LocationChangeListener()
    private val networkLocationListener = LocationChangeListener()

    companion object {
        const val ARG_FUSED_LOCATION = "is_fused_location_api"
        private const val TWO_MINUTES = 1000 * 60 * 2
        private const val MIN_BEARING_DIFF = 2.0f
        private const val FASTEST_INTERVAL_IN_MS = 1000L
    }

    private var axisX :Int = 0
    private var axisY :Int = 0
    private var bearing :Float = 0f
    private var isFusedLocationApi :Boolean = false
    private var currentBestLocation :Location? = null
    private var locationCallback :LocationCallback? = null
    private var mFusedLocationClient :FusedLocationProviderClient? = null
    override fun onBind(intent :Intent?) = localBinder

    override fun onStartCommand(intent :Intent?, flags :Int, startId :Int) :Int {
        super.onStartCommand(intent, flags, startId)
        if(intent != null && intent.hasExtra(ARG_FUSED_LOCATION))
            isFusedLocationApi = intent.getBooleanExtra(ARG_FUSED_LOCATION, false)
        onCreate()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        if(checkIfLocationIsEnabled())
            getLocation()
        else
            locationCallback?.locationDisabled()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFusedLocationApi) {
            mFusedLocationClient?.removeLocationUpdates(fusedLocationListener)
            locationCallback = null
        } else
            stopUpdates()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if(isFusedLocationApi) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val mLocationRequest = LocationRequest()
            mLocationRequest.interval = TWO_MINUTES.toLong()
            mLocationRequest.fastestInterval = FASTEST_INTERVAL_IN_MS
            mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, fusedLocationListener, Looper.myLooper());
        } else {
            val lastKnownGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val lastKnownNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            var bestLastKnownLocation = currentBestLocation

            if(lastKnownGpsLocation != null && isBetterLocation(lastKnownGpsLocation, bestLastKnownLocation)) {
                bestLastKnownLocation = lastKnownGpsLocation
            }

            if(lastKnownNetworkLocation != null && isBetterLocation(lastKnownNetworkLocation, bestLastKnownLocation)) {
                bestLastKnownLocation = lastKnownNetworkLocation
            }

            currentBestLocation = bestLastKnownLocation

            if(locationManager.allProviders.contains(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, FASTEST_INTERVAL_IN_MS, 0.0f, gpsLocationListener)
            }

            if(locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, FASTEST_INTERVAL_IN_MS, 0.0f, networkLocationListener)
            }

            bestLastKnownLocation?.bearing = bearing
            locationCallback?.onSuccess(currentBestLocation as Location)
        }
        val mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL * 5)
    }

    fun setLocationCallback(callback :LocationCallback?) {
        locationCallback = callback
    }

    fun stopUpdates() {
        locationManager.removeUpdates(gpsLocationListener)
        locationManager.removeUpdates(networkLocationListener)
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun isBetterLocation(location :Location, currentBestLocation :Location?) :Boolean {
        if(currentBestLocation == null)
            return true
        val timeDelta = location.time - currentBestLocation.time
        val isSignificantlyNewer = timeDelta > TWO_MINUTES
        val isSignificantlyOlder = timeDelta < -TWO_MINUTES
        val isNewer = timeDelta > 0

        if(isSignificantlyNewer) {
            return true
        } else if(isSignificantlyOlder) {
            return false
        }
        val accuracyDelta = (location.accuracy - currentBestLocation.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200
        val isFromSameProvider = isSameProvider(location.provider, currentBestLocation.provider)

        if(isMoreAccurate) {
            return true
        } else if(isNewer && !isLessAccurate) {
            return true
        } else if(isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true
        }
        return false
    }

    private fun isSameProvider(provider1 :String?, provider2 :String?) :Boolean = if(provider1 == null) provider2 == null else provider1 == provider2

    private fun checkIfLocationIsEnabled() :Boolean = (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

    private inner class LocationChangeListener :android.location.LocationListener {
        override fun onLocationChanged(location :Location?) {
            if(location == null)
                return

            if(isBetterLocation(location, currentBestLocation)) {
                currentBestLocation = location
                currentBestLocation?.bearing = bearing
                locationCallback?.onSuccess(currentBestLocation as Location)
            }
        }

        override fun onProviderDisabled(provider :String?) {}
        override fun onProviderEnabled(provider :String?) {}
        override fun onStatusChanged(provider :String?, status :Int, extras :Bundle?) {}
    }

    private inner class SensorListener :SensorEventListener {
        override fun onAccuracyChanged(sensor :Sensor?, accuracy :Int) {
            if(sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                Log.e(Locations::class.java.simpleName, "Rotation sensor accuracy changed to: $accuracy")
            }
        }

        override fun onSensorChanged(event :SensorEvent?) {
            val rotationMatrix = FloatArray(16)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event?.values)
            val orientationValues = FloatArray(3)

            readDisplayRotation()

            SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisY, rotationMatrix)
            SensorManager.getOrientation(rotationMatrix, orientationValues)
            val azimuth = Math.toDegrees(orientationValues[0].toDouble())
            val abs = Math.abs(bearing.minus(azimuth).toFloat()) > MIN_BEARING_DIFF

            if(abs) {
                bearing = azimuth.toFloat()
                currentBestLocation?.bearing = bearing
            }
        }
    }

    private inner class MyLocationListener :com.google.android.gms.location.LocationCallback() {
        override fun onLocationResult(location :LocationResult?) {
            super.onLocationResult(location)
            if(location == null)
                return

            if(isBetterLocation(location.lastLocation, currentBestLocation)) {
                currentBestLocation = location.lastLocation
                currentBestLocation?.bearing = bearing
                locationCallback?.onSuccess(currentBestLocation as Location)
            }
        }
    }

    private fun readDisplayRotation() {
        axisX = SensorManager.AXIS_X
        axisY = SensorManager.AXIS_Y
        when(windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> {
                axisX = SensorManager.AXIS_Y
                axisY = SensorManager.AXIS_MINUS_X
            }
            Surface.ROTATION_180 -> axisY = SensorManager.AXIS_MINUS_Y
            Surface.ROTATION_270 -> {
                axisX = SensorManager.AXIS_MINUS_Y
                axisY = SensorManager.AXIS_X
            }
        }
    }

    inner class LocalBinder :Binder() {
        val service :Locations
            get() = this@Locations
    }

    //Address
    private fun Context.getGeoCoderAddress() :List<Address>? {
        val geoCoder = Geocoder(this, Locale.ENGLISH)
        try {
            return geoCoder.getFromLocation(currentBestLocation?.latitude
                ?: 0.0, currentBestLocation?.longitude ?: 0.0, 1)
        } catch(e :IOException) {
            Log.e(Locations::class.java.simpleName, "Impossible to connect to Geocoder: $e")
        }

        return null
    }

    private fun Context.getFirstAddress() :Address? {
        val addresses = getGeoCoderAddress()
        return if(addresses != null && addresses.isNotEmpty())
            addresses[0]
        else null
    }

    fun Context.getAddressLine() :String = getFirstAddress()?.getAddressLine(0) ?: ""
    fun Context.getLocality() :String = getFirstAddress()?.locality ?: ""
    fun Context.getPostalCode() :String = getFirstAddress()?.postalCode ?: ""
    fun Context.getCountryName() :String = getFirstAddress()?.countryName ?: ""
    interface LocationCallback {
        fun locationDisabled()
        fun onSuccess(location :Location)
        fun onFailure(e :Exception)
    }
}
