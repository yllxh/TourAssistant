package com.yllxh.tourassistant.utils

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.data.model.Location
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import java.lang.IllegalArgumentException
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

fun GoogleMap.animateCamera(place: Place, zoom: Float = DEFAULT_ZOOM) {
    this.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            place.location.toLatLng(),
            zoom
        )
    )
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

fun String.toggleStrings(str1: String, str2: String): String =
    when (this) {
        str1 -> str2
        str2 -> str1
        else -> throw IllegalArgumentException("Neither $str1 nor $str2 are equal to $this")
    }
