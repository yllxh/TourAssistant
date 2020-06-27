package com.yllxh.tourassistant.screens.selectplacemap

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import com.yllxh.tourassistant.utils.getAddressAt
import kotlinx.coroutines.*

enum class REQUEST {
    STARTED, FINISHED, FAILED, UNKNOWN
}

class SelectPlaceMapViewModel(selectedPlace: Place, app: Application) : AndroidViewModel(app) {

    private val _selectedPlace = MutableLiveData(selectedPlace)
    val selectedPlace: LiveData<Place> get() = _selectedPlace

    private val _fetchingInfo = MutableLiveData(REQUEST.UNKNOWN)
    val fetchingInfo: LiveData<REQUEST> get() = _fetchingInfo

    init {
        if (selectedPlace.location.address.isBlank()){
            searchAddressAt(selectedPlace.location.toLatLng())
        }
    }


    private fun searchAddressAt(latLng: LatLng) = viewModelScope.launch {
        _fetchingInfo.value = REQUEST.STARTED
        withContext(Dispatchers.IO) {
            val address = getAddressAt(getApplication(), latLng)
            if (address.isBlank()) {
                _fetchingInfo.postValue(REQUEST.FAILED)
                return@withContext
            }

            _selectedPlace.value?.let {
                val placeWithAddress = it.apply { it.fillMissingInfo(address) }
                _selectedPlace.postValue(placeWithAddress)
            }
            _fetchingInfo.postValue(REQUEST.FINISHED)
        }
    }

    fun setSelectedPlace(place: Place) {
        _selectedPlace.value?.let {
            _selectedPlace.value = place.copy(
                placeId = it.placeId,
                _importance = it.importance,
                toDos = it.toDos
            )
        }

        if (place.hasMissingInfo()) {
            searchAddressAt(place.location.toLatLng())
        }
    }
}