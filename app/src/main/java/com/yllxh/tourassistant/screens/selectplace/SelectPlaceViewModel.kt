package com.yllxh.tourassistant.screens.selectplace

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.utils.getAddressAt
import kotlinx.coroutines.*

class SelectPlaceViewModel(app: Application) : AndroidViewModel(app) {
    private val _selectedPlace = MutableLiveData<PlaceDB>()
    val selectedPlace: LiveData<PlaceDB> get() = _selectedPlace

    fun searchAddressAt(latLng: LatLng) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val address = getAddressAt(getApplication(), latLng)
            if (address.address.isEmpty())
                return@withContext

            _selectedPlace.value?.let {
                val placeWithAddress = it.apply { this.location.address = address }
                _selectedPlace.postValue(placeWithAddress)
            }
        }
    }

    fun setSelectedPlace(selectedPlace: PlaceDB) {
        _selectedPlace.value = selectedPlace

        if (selectedPlace.location.addressAsString.isEmpty()) {
            searchAddressAt(selectedPlace.location.toLatLng())
        }
    }
}