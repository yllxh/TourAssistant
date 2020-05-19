package com.yllxh.tourassistant.screens.selectplace

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.model.AddressLatLng
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.utils.getAddressAt
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class SelectPlaceViewModel(app: Application) : AndroidViewModel(app) {
    private val _selectedPlace = MutableLiveData<PlaceDB>()
    val selectedPlace: LiveData<PlaceDB> get() = _selectedPlace

    fun searchAddressAt(latLng: LatLng) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val address: String = getAddressAt(getApplication(), latLng)
            _selectedPlace.value?.let {
                val placeWithAddress = it.apply { this.location.address = address }
                _selectedPlace.postValue(placeWithAddress)
            }
        }
    }

    fun setSelectedPlace(selectedPlace: PlaceDB) {
        _selectedPlace.value = selectedPlace

        if (selectedPlace.location.address.isEmpty()) {
            searchAddressAt(selectedPlace.location.toLatLng())
        }
    }
}