package com.yllxh.tourassistant.utils

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.data.model.Location
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.pow
import com.google.android.libraries.places.api.model.Place as GoogleApi_Place

private const val DEFAULT_ZOOM = 15f

fun <T> Fragment.observe(
    liveData: LiveData<T>,
    block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, block)
}

fun View.setBackGroundColorTo(resId: Int) {
    setBackgroundColor(ContextCompat.getColor(context, resId))
}

fun GoogleMap.animateCameraAt(place: Place, zoom: Float = DEFAULT_ZOOM) {
    this.animateCameraAt(place.location.toLatLng(), zoom)
}
fun GoogleMap.animateCameraAt(latLng: LatLng, zoom: Float = DEFAULT_ZOOM) {
    val camZoom = cameraPosition.zoom
    this.animateCamera(
        CameraUpdateFactory.newLatLngZoom(latLng, max(camZoom, zoom)))
}
fun Location.toMapsModelLatLng(): com.google.maps.model.LatLng {
    return com.google.maps.model.LatLng(latitude, longitude)
}

fun GoogleMap.addSimpleMarker(place: Place): Marker {
    val position = MarkerOptions().position(place.location.toLatLng())
    return addMarker(position).apply {
        title = when {
            !place.name.isBlank() -> place.name
            else -> place.location.addressAsString
        }
    }
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun GoogleApi_Place.toPlace(): Place {
    val latitude = latLng?.latitude ?: Double.MAX_VALUE
    val longitude = latLng?.longitude ?: Double.MAX_VALUE

    return Place(name = name ?: "").apply {
        location = Location(
            latitude = latitude,
            longitude = longitude,
            address = Address(address = address ?: "")
        )
    }
}

private fun Fragment.showAlertDialogForLocationPermission(
    onOk: () -> Unit = {},
    onDeny: () -> Unit = {}
) {
    AlertDialog.Builder(requireContext())
        .setMessage("Permission is required so to show you location on the map.")
        .setPositiveButton("ok") { _, _ ->
            onOk()
        }
        .setNegativeButton("Deny") { _, _ ->
            onDeny()
        }.create().show()
}

fun Fragment.onMissingLocationPermission(onGranted: () -> Unit = {}) {
    requestLocationPermission(
        onGranted = onGranted,
        onDenied = { isPermanent ->
            showAlertDialogForLocationPermission (onOk = {

                if (!isPermanent) {
                    onMissingLocationPermission()
                } else {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.fromParts("package", "com.yllxh.tourassistant", null)

                        startActivity(this)
                    }
                }
            })
        },
        onRationaleShouldBeShown = {
            if (it != null)
                showAlertDialogForLocationPermission(
                    it::continuePermissionRequest,
                    it::cancelPermissionRequest
                )
        })
}
fun ImageView.setColor(id: Int) = setColorFilter(ContextCompat.getColor(context, id))


fun String.toggleStrings(str1: String, str2: String): String =
    when (this) {
        str1 -> str2
        str2 -> str1
        else -> throw IllegalArgumentException("Neither $str1 nor $str2 are equal to $this")
    }

fun onMainThread(runnable: () -> Unit){
    Handler(Looper.getMainLooper()).post(runnable)
}

fun Location.toPlace(): Place {
    return Place(name = this.addressAsString, location = this)
}

fun Double.isEqualTo(other: Double, precision:Int = 6): Boolean {
    val pow = 10.0.pow(precision)
    return (this * pow).toInt() == (other * pow).toInt()
}
fun com.google.maps.model.LatLng.isEqualTo(other: com.google.maps.model.LatLng, precision:Int = 6)
        = (lat.isEqualTo(other.lat) && lng.isEqualTo(other.lng, precision))

fun com.google.maps.model.LatLng.toLatLng(): LatLng {
    return LatLng(lat, lng)
}

fun TabLayout.onTabSelected(block: (Int) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            block(tab?.position ?: return)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    })
}
fun TabLayout.selectTabAt(position: Int) {
    getTabAt(position)?.select()
}
