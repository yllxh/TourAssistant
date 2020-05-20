package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    var address: String = "",
    var city: String = "",
    var country: String = "",
    var countryCode: String = ""
) : Parcelable