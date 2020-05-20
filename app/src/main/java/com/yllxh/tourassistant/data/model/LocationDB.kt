package com.yllxh.tourassistant.data.model

import android.os.Parcelable
import androidx.room.Embedded
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDB(
    var latitude: Double = Double.MAX_VALUE,
    var longitude: Double = Double.MAX_VALUE,
    @Embedded
    var address: Address = Address()
) : Parcelable {
    @IgnoredOnParcel var countryCode: String
        get() = address.countryCode
        set(value) { address.countryCode = value }
    @IgnoredOnParcel var addressAsString: String
        get() = address.address
        set(value) { address.address = value }

    @IgnoredOnParcel var country: String
        get() = address.country
        set(value) { address.country = value }
    @IgnoredOnParcel var city: String
        get() = address.city
        set(value) { address.city = value }

    fun isValid(): Boolean {
        return (latitude > -90 || latitude < 90)
                && (longitude > -180 || longitude < 180)
    }

    fun toLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }

    fun fillMissingInfo(address: Address){
        if (addressAsString.isEmpty()
            || addressAsString.isBlank()){

            addressAsString = address.address
        }
        if (country.isBlank() ||
            country.isEmpty()){
            country = address.country
        }
        if (countryCode.isBlank() ||
            countryCode.isEmpty()){
            countryCode = address.countryCode
        }
        if (city.isBlank() ||
            city.isEmpty()){
            city = address.city
        }
    }
}