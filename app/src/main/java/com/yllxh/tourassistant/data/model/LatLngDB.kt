package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LatLngDB(
    val latitude: Double = -1.0,
    val longitude: Double = -1.0
) : Parcelable
