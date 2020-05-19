package com.yllxh.tourassistant.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.model.AddressLatLng
import com.yllxh.tourassistant.screens.selectplace.SelectPlaceViewModel
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

fun getAddressAt(context: Context, latLng: LatLng): String {
    val geocoder = Geocoder(
        context,
        Locale.getDefault()
    )
    var addresses: List<Address>? = null

    val addressLatLng = AddressLatLng("", latLng)

    try {
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    } catch (ioException: IOException) {
        Log.e(SelectPlaceViewModel::class.simpleName, "Service Not Available", ioException)
    } catch (illegalArgumentException: IllegalArgumentException) {
        Log.e(
            SelectPlaceViewModel::class.simpleName, "Invalid LatLng: " +
                    "Latitude = ${latLng.latitude}" +
                    ", Longitude = ${latLng.longitude}", illegalArgumentException
        )
    }

    if (addresses != null && addresses.isNotEmpty()) {
        val address = addresses[0]
        val addressParts = ArrayList<String?>()

        for (i in 0..address.maxAddressLineIndex) {
            addressParts.add(address.getAddressLine(i))
        }
        addressLatLng.address = TextUtils.join("\n", addressParts)
    }

    return addressLatLng.address
}
