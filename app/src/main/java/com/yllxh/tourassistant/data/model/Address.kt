package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.utils.isEmptyOrBlank
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    var address: String = "",
    var city: String = "",
    var country: String = "",
    var countryCode: String = ""
) : Parcelable {
    fun isEmpty(): Boolean{
        return address.isEmpty()
                && city.isEmpty()
                && country.isEmpty()
                && countryCode.isEmpty()
    }
    fun hasMissingInfo(): Boolean{
        return address.isEmpty()
                || city.isEmpty()
                || country.isEmpty()
                || countryCode.isEmpty()
    }

    fun fillMissingInfo(other: Address) {
        if (address.isEmptyOrBlank()){
            address = other.address
        }
        if (country.isEmptyOrBlank()){
            country = other.country
        }
        if (countryCode.isEmptyOrBlank()){
            countryCode = other.countryCode
        }
        if (city.isEmptyOrBlank()){
            city = other.city
        }
    }
}