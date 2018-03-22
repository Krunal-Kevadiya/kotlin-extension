package com.kotlinextension.ui.map

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.util.Log
import com.extensions.content.Permission
import com.extensions.content.PermissionExtensions
import com.extensions.dialogs.alert
import com.extensions.dialogs.cancelButton
import com.extensions.dialogs.customButton
import com.extensions.dialogs.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.databinding.ActivityMapsBinding

class MapsActivity : BaseActivity<ActivityMapsBinding, MapsViewModel>(), MapsNavigator, OnMapReadyCallback, LifecycleOwner {
	private lateinit var mMap: GoogleMap
	private var mCountUpdated: Int = 0

	companion object {
		fun getActivity(context: Context): Intent = Intent(context, MapsActivity::class.java)
	}

	override fun getLayoutId(): Int = R.layout.activity_maps

	override fun getBindingVariable(): Int = BR.viewModel

	override fun getViewModel(): MapsViewModel {
		return ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		finish()
	}

	override fun initObserve() {
		val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)

		val callback = object : (Int, Array<Permission>) -> Unit {
			override fun invoke(resultCode: Int, list: Array<Permission>) {
				when (resultCode) {
					PermissionExtensions.PERMISSION_GRANTED -> { start() }
					PermissionExtensions.PERMISSION_RATIONALE -> {
                        val call = this
                        alert(getString(R.string.permission_desc_rational_location), getString(R.string.permission_title_location)) {
                            isCancelable = false
                            customButton(R.string.btn_proceed) {
                                PermissionExtensions.instance.requestPermission(this@MapsActivity, list, call)
                            }
                            cancelButton {}
                        }
                    }
					PermissionExtensions.PERMISSION_SETTING -> {
                        alert(getString(R.string.permission_desc_setting_location), getString(R.string.permission_title_location)) {
                            isCancelable = false
                            customButton(R.string.btn_setting) {
                                startActivity(PermissionExtensions.settingIntent(this@MapsActivity))
                            }
                            cancelButton {}
                        }
                    }
					PermissionExtensions.PERMISSION_FAILED -> toast(R.string.permission_error_location)
				}
			}
		}
		val arrays: Array<Permission> = arrayOf(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
		PermissionExtensions.instance.checkAndRequestPermission(this@MapsActivity, arrays, callback)
	}

	override fun backActivity() {
		onBackPressed()
	}

	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
	}

	private fun start() {
		//This for Live data
		mViewModel.initLocation(this)
		mViewModel.location.observe(this, Observer {
			mCountUpdated++
			Log.e("MapsActivity", "Location Update - $mCountUpdated")
			it?.let {
				mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Current location - $mCountUpdated time update."))
				mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
			}
		})
		//This for Lifecycle
		/*lifecycle.addObserver(MyLocationUpdateObserver(this, false, object : Locations.LocationCallback {
			override fun locationDisabled() {
				shortToast(R.string.disable_location)
			}

			override fun onSuccess(location :Location) {
				mCountUpdated++
				Log.e("MapsActivity", "Location Update - $mCountUpdated")
				val sydney = LatLng(location.latitude, location.longitude)
				mMap.addMarker(MarkerOptions().position(sydney).title("Current location - $mCountUpdated time update."))
				mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
			}

			override fun onFailure(e :Exception) {
				Log.e("MapsActivity", e.toString())
			}
		}))*/
	}
}
