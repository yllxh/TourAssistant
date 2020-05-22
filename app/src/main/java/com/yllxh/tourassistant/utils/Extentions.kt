package com.yllxh.tourassistant.utils

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.data.model.Location
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.google.android.libraries.places.api.model.Place as GoogleApi_Place

private const val DEFAULT_ZOOM = 15f

/**
 * Extension function for the [LifecycleOwner] class, to allow them to
 * observe [LiveData] with a more readable syntax.
 *
 * @param liveData          The LiveData which is to be observed.
 * @param block             The block of code to be run each time the LiveData is updated.
 */
fun <T> Fragment.observe(
    liveData: LiveData<T>,
    block: (T) -> Unit
) {
    liveData.observe(viewLifecycleOwner, Observer(block))
}

fun View.setBackGroundColorTo(resId: Int) {
    setBackgroundColor(ContextCompat.getColor(context, resId))
}

fun String.isEmptyOrBlank(): Boolean {
    return isEmpty() || isBlank()
}

fun GoogleMap.animateCamera(place: Place, zoom: Float = DEFAULT_ZOOM) {
    this.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            place.location.toLatLng(),
            zoom
        )
    )
}

fun GoogleMap.addSimpleMarker(place: Place): Marker {
    val position = MarkerOptions().position(place.location.toLatLng())
    return addMarker(position).apply {
        title = when{
            !place.name.isEmptyOrBlank() -> place.name
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

    val place = Place()
    place.name = name ?: ""
    place.location = Location(
        latitude = latitude,
        longitude = longitude,
        address = Address(address = address ?: "")
    )

    return place
}