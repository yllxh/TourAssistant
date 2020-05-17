package com.yllxh.tourassistant.utils

import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*

class LocationRetriever(
    private val fragment: Fragment,
    private val onLocationReceived: (Location) -> Unit
) : LifecycleObserver {

    private var keepTrackOfUser: Boolean = false
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var locationReceivedCallBack: LocationCallback? = null

    init {
        fragment.lifecycle.addObserver(this)
        requestDeviceLocation()
    }

    private fun requestDeviceLocation() {

        if (locationReceivedCallBack == null) {
            initLocationReceivedCallBack()
        }
        if (fusedLocationProvider == null) {
            fusedLocationProvider =
                LocationServices.getFusedLocationProviderClient(fragment.requireContext())
        }
        fusedLocationProvider?.requestLocationUpdates(
            getLocationRequest(),
            locationReceivedCallBack,
            null
        )
    }

    private fun getLocationRequest(): LocationRequest? {
        return LocationRequest().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun initLocationReceivedCallBack() {
        locationReceivedCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let {
                    if (it == null) return@let

                    onLocationReceived(it)
                    if (!keepTrackOfUser)
                        fusedLocationProvider.stop()
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        if (keepTrackOfUser) {
            requestDeviceLocation()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        if (keepTrackOfUser) {
            fusedLocationProvider.stop()
        }
    }

    private fun FusedLocationProviderClient?.stop() {
        this?.removeLocationUpdates(locationReceivedCallBack)
    }

    fun toggleTracking() {
        if (keepTrackOfUser) {
            fusedLocationProvider.stop()
            keepTrackOfUser = false
        } else {
            keepTrackOfUser = true
            requestDeviceLocation()
        }
    }
}