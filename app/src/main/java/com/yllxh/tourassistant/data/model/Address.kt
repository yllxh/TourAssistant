package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    var address: String = "",
    var city: String = "",
    var country: String = "",
    var countryCode: String = ""
) : Parcelable {
    fun isBlank(): Boolean{
        return address.isBlank()
                && city.isBlank()
                && country.isBlank()
                && countryCode.isBlank()
    }
    fun hasMissingInfo(): Boolean{
        return address.isBlank()
                || city.isBlank()
                || country.isBlank()
                || countryCode.isBlank()
    }

    fun fillMissingInfo(other: Address) {
        if (address.isBlank()){
            address = other.address
        }
        if (country.isBlank()){
            country = other.country
        }
        if (countryCode.isBlank()){
            countryCode = other.countryCode
        }
        if (city.isBlank()){
            city = other.city
        }
    }
}