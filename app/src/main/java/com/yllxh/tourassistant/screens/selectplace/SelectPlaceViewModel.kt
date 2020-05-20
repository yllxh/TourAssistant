package com.yllxh.tourassistant.screens.selectplace

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.model.Address
import com.yllxh.tourassistant.data.source.local.database.entity.PlaceDB
import com.yllxh.tourassistant.utils.getAddressAt
import kotlinx.coroutines.*

class SelectPlaceViewModel(selectedPlace: PlaceDB, app: Application) : AndroidViewModel(app) {

    private val _selectedPlace = MutableLiveData(selectedPlace)
    val selectedPlace: LiveData<PlaceDB> get() = _selectedPlace

    init {
        if (selectedPlace.location.address.isEmpty()){
            searchAddressAt(selectedPlace.location.toLatLng())
        }
    }

    private fun searchAddressAt(latLng: LatLng) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val address = getAddressAt(getApplication(), latLng)
            if (address.isEmpty())
                return@withContext

            _selectedPlace.value?.let {
                val placeWithAddress = it.apply { it.fillMissingInfo(address) }
                _selectedPlace.postValue(placeWithAddress)
            }
        }
    }

    fun setSelectedPlace(selectedPlace: PlaceDB) {
        _selectedPlace.value = selectedPlace

        if (selectedPlace.location.address.hasMissingInfo()) {
            searchAddressAt(selectedPlace.location.toLatLng())
        }
    }
}