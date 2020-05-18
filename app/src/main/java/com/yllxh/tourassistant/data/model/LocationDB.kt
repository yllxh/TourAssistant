package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDB(
    @Embedded var LatLngDB: LatLngDB = LatLngDB(),
    var country: String = "",
    var city: String = ""
) : Parcelable