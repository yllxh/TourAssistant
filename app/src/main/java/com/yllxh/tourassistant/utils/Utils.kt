package com.yllxh.tourassistant.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken

import com.karumi.dexter.listener.PermissionDeniedResponse

import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest

import com.karumi.dexter.listener.single.PermissionListener




fun Context.hasLocationPermission(): Boolean {

    return ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.requestLocationPermission(
    onGranted: () -> Unit = {},
    onDenied: (isPermanent: Boolean) -> Unit = {},
    onRationaleShouldBeShown: (token: PermissionToken?) -> Unit = {}
) {
    Dexter.withContext(requireContext())
        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                onGranted()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                onDenied(response.isPermanentlyDenied)
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {
                onRationaleShouldBeShown(token)
            }
        }).check()
}