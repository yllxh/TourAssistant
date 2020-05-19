package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressLatLng(var address: String, val latLng: LatLng) : Parcelable