package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDB(
    var latitude: Double = -1.0,
    var longitude: Double = -1.0,
    var country: String = "",
    var city: String = ""
) : Parcelable {
    fun isValid(): Boolean {
        return latitude > 0 && longitude > 0
    }
}