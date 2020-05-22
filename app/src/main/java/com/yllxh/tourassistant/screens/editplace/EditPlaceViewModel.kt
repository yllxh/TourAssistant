package com.yllxh.tourassistant.screens.editplace

import android.app.Application
import androidx.lifecycle.*
import com.yllxh.tourassistant.data.source.local.database.entity.Place
import kotlinx.coroutines.launch

class EditPlaceViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = PlaceRepository(app.applicationContext)

    fun saveChangesToPlace(place: Place) {
        viewModelScope.launch {
            repository.saveChangesToPlace(place)
        }
    }

}