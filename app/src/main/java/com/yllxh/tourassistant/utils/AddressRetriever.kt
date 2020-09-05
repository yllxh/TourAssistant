package com.yllxh.tourassistant.utils

import android.content.Context
import android.location.Address as AndroidAddress
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.screens.selectplacemap.SelectPlaceMapViewModel
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

fun LatLng.getAddress(context: Context): Address {
    var addresses: List<AndroidAddress>? = null
    try {
        addresses = Geocoder(
            context,
            Locale.getDefault()
        ).getFromLocation(latitude, longitude, 1)
    } catch (ioException: IOException) {
        Log.e(SelectPlaceMapViewModel::class.simpleName, "Service Not Available", ioException)
    } catch (illegalArgumentException: IllegalArgumentException) {
        Log.e(
            SelectPlaceMapViewModel::class.simpleName, "Invalid LatLng: " +
                    "Latitude = $latitude" +
                    ", Longitude = $longitude", illegalArgumentException
        )
    }

    val addressRetrieved = Address()

    if (addresses != null && addresses.isNotEmpty()) {
        val address = addresses[0]
        val addressParts = ArrayList<String?>()

        for (i in 0..address.maxAddressLineIndex) {
            addressParts.add(address.getAddressLine(i))
        }
        addressRetrieved.apply {
            this.address = TextUtils.join("\n", addressParts)
            this.city = address.locality ?: ""
            this.country = address.countryName ?: ""
            this.countryCode = address.countryCode ?: ""
        }
    }

    return addressRetrieved
}
