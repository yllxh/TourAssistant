package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import androidx.room.Embedded
import com.yllxh.tourassistant.data.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @Embedded var latLng: LatLng = LatLng(),
    var country: String = "",
    var city: String = ""
) : Parcelable