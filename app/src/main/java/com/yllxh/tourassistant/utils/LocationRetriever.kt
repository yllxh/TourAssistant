package com.yllxh.tourassistant.utils

import android.annotation.SuppressLint
import android.location.Location
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.*

class LocationRetriever(
    private val fragment: Fragment,
    var keepTrackOfUser: Boolean = false,
    var intervalInMilliseconds: Long = 10000L,
    private val onMissingPermission: () -> Unit,
    private val onLocationReceived: (Location) -> Unit
) : LifecycleObserver {
    private var isWorking = false
    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var locationReceivedCallBack: LocationCallback? = null

    private var locationPermissionGranted: Boolean = false

    init {
        fragment.lifecycle.addObserver(this)
    }

    @SuppressLint("MissingPermission")
    fun requestDeviceLocation() {
        if (!fragment.requireContext().hasLocationPermission()) {
            locationPermissionGranted = false
            isWorking = false
            onMissingPermission()
            return
        }
        locationPermissionGranted = true
        isWorking = true

        if (fusedLocationProvider == null) {
            fusedLocationProvider =
                LocationServices.getFusedLocationProviderClient(fragment.requireContext())
        }

        if (locationReceivedCallBack == null) {
            initLocationReceivedCallBack()
        }

        fusedLocationProvider?.requestLocationUpdates(
            getLocationRequest(),
            locationReceivedCallBack,
            null
        )

    }

    private fun getLocationRequest(): LocationRequest? {
        return LocationRequest().apply {
            interval = intervalInMilliseconds
            fastestInterval = intervalInMilliseconds / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun initLocationReceivedCallBack() {
        locationReceivedCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let {
                    if (it == null) return@let

                    onLocationReceived(it)
                    if (!keepTrackOfUser) {
                        fusedLocationProvider?.removeLocationUpdates(locationReceivedCallBack)
                        isWorking = false
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        if (isWorking) {
            requestDeviceLocation()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        if (isWorking) {
            fusedLocationProvider?.removeLocationUpdates(locationReceivedCallBack)
        }
    }
}