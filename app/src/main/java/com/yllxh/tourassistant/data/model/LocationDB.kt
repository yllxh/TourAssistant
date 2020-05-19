package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import androidx.room.Embedded
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDB(
    var latitude: Double = Double.MAX_VALUE,
    var longitude: Double = Double.MAX_VALUE,
    var address: String = "",
    var country: String = "",
    var city: String = ""
) : Parcelable {
    fun isValid(): Boolean {
        return (latitude > -90 || latitude < 90)
                && (longitude > -180 || longitude < 180)
    }

    fun toLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }
}